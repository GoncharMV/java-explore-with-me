package ru.practicum.utils.stats;


import ru.practicum.dto.StatsOutputDto;
import ru.practicum.events.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatsService {
    void addHit(HttpServletRequest request);

    List<StatsOutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    Map<Long, Long> getViews(List<Event> events);

}