package ru.practicum.utils.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatClient;
import ru.practicum.dto.StatsOutputDto;
import ru.practicum.events.model.Event;
import ru.practicum.utils.ConstantUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatClient statsClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value(value = "${app.name}")
    private String appName;

    @Override
    public void addHit(HttpServletRequest request) {
        statsClient.addHit(appName, request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.parse(LocalDateTime.now().format(ConstantUtil.DATA_FORMATTER), ConstantUtil.DATA_FORMATTER));
    }

    @Override
    public List<StatsOutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<Object> response = statsClient.getStats(start, end, uris, unique);

        try {
            return Arrays.asList(mapper.readValue(mapper.writeValueAsString(response.getBody()),
                    StatsOutputDto[].class));
        } catch (IOException exception) {
            throw new ClassCastException(exception.getMessage());
        }
    }

    @Override
    public Map<Long, Long> getViews(List<Event> events) {
        Map<Long, Long> views = new HashMap<>();

        List<Event> publishedEvents = getPublished(events);

        if (events.isEmpty()) {
            return views;
        }

        Optional<LocalDateTime> minDate = publishedEvents.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        if (minDate.isPresent()) {
            LocalDateTime start = minDate.get();
            LocalDateTime end = LocalDateTime.now();
            List<String> uris = publishedEvents.stream()
                    .map(Event::getId)
                    .map(id -> ("/events/" + id))
                    .collect(Collectors.toList());

            List<StatsOutputDto> stats = getStats(start, end, uris, true);
            stats.forEach(stat -> {
                Long eventId = Long.parseLong(stat.getUri()
                        .split("/", 0)[2]);
                views.put(eventId, views.getOrDefault(eventId, 0L) + stat.getHits());
            });
        }

        return views;
    }

    private List<Event> getPublished(List<Event> events) {
        return events.stream()
                .filter(event -> event.getPublishedOn() != null)
                .collect(Collectors.toList());
    }
}