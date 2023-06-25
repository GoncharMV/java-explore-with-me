package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.users.model.User;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.mapper.EventMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
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
    public EventOutputFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest requestDto) {
        return null;
    }

    @Override
    public List<EventOutputFullDto> adminFindEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);



        List<Event> events = eventRepository.adminFindEvents(users, states, categories, rangeStart, rangeEnd, pageable);
        return EventMapper.toEventFullDtoList(events);
    }

    @Override
    public List<EventShortDto> initiatorGetEvents(Long userId, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);
        User initiator = findEntity.findUserOrElseThrow(userId);

        List<Event> events = eventRepository.findAllByInitiator(initiator, pageable);

        return EventMapper.toEventShortList(events);
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorAddEvent(Long userId, EventInputDto requestDto) {
        User initiator = findEntity.findUserOrElseThrow(userId);
        Category cat = findEntity.findCategoryOrElseThrow(requestDto.getCategoryId());
        Location loc = locationService.getLocationOrElseSave(requestDto.getLocation());

        Event event = EventMapper.toEvent(requestDto, initiator, cat, loc);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        event = eventRepository.save(event);

        return EventMapper.toOutputDto(event);
    }

    @Override
    public EventOutputFullDto initiatorGetEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest requestDto) {
        return null;
    }

    @Override
    public List<EventOutputFullDto> findEvents(String text, List<Long> categories, Boolean paid,
                                               String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                               String sort, int from, int size) {
        return null;
    }

    @Override
    public EventOutputFullDto getEvent(Long id) {
        return null;
    }


}
