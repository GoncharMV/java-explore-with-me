package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.users.model.User;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final FindEntityUtilService findEntity;
    private final LocationService locationService;

    @Override
    @Transactional
    public EventOutputDto adminUpdateEvent(Long eventId, EventInputDto requestDto) {
        return null;
    }

    @Override
    public List<EventOutputDto> adminFindEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                                String rangeStart, String rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventOutputDto initiatorGetEvents(Long userId, int from, int size) {
        return null;
    }

    @Override
    @Transactional
    public EventOutputDto initiatorAddEvent(Long userId, EventInputDto requestDto) {
        User initiator = findEntity.findUserOrElseThrow(userId);
        Category cat = findEntity.findCategoryOrElseThrow(requestDto.getCategoryId());
        Location loc = locationService.getLocationOrElseSave(requestDto.getLocation());

        Event event = EventMapper.toEvent(requestDto, initiator, cat, loc);

        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());

        event = eventRepository.save(event);

        return EventMapper.toOutputDto(event);
    }

    @Override
    public EventOutputDto initiatorGetEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventOutputDto initiatorUpdateEvent(Long userId, Long eventId, EventInputDto requestDto) {
        return null;
    }

    @Override
    public List<EventOutputDto> findEvents(String text, List<Integer> categories, Boolean paid,
                                           String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                           String sort, int from, int size) {
        return null;
    }

    @Override
    public EventOutputDto getEvent(Long id) {
        return null;
    }


}
