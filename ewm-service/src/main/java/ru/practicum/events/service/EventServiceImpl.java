package ru.practicum.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.participation_request.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.RequestNotProcessedException;
import ru.practicum.utils.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final FindEntityUtilService findEntity;
    private final EventRepository eventRepository;
    private final LocationService locationService;

    @Override
    @Transactional
    public EventOutputFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest dto) {
        Event event = findEntity.findEventOrElseThrow(eventId);

        if (dto.getEventDate() != null) findEntity.checkEventDate(dto.getEventDate());

        if (dto.getStateAction() != null) adminUpdateEventStatus(event, dto.getStateAction());

        Event updateEvent = updateEvent(event, dto);

        return EventMapper.toOutputDto(updateEvent, getRequestList(event));
    }

    @Override
    public List<EventOutputFullDto> adminFindEvents(List<Long> users, List<EventState> states,
                                                    List<Long> categories, LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd, int from, int size) {

        Pageable pageable = PageableUtil.pageManager(from, size, null);

        EventQueryCriteria criteria = EventQueryCriteria.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .build();

        List<BooleanExpression> conditions = new ArrayList<>();

        if (criteria.getUsers() != null) {
            conditions.add(QEvent.event.initiator.id.in(criteria.getUsers()));
        }

        if (criteria.getStates() != null) {
            conditions.add(QEvent.event.state.in(criteria.getStates()));
        } else {
            conditions.add(QEvent.event.state.in(List.of(EventState.values())));
        }

        fillCommonCriteria(criteria, conditions);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("невалидная дата");
            }
        }

        BooleanExpression finalCond = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = eventRepository.findAll(finalCond, pageable).getContent();

        return EventMapper.toEventFullDtoList(events, findEntity.findConfirmedRequestsMap(events));
    }

    @Override
    public List<EventShortDto> initiatorGetEvents(Long userId, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);
        User initiator = findEntity.findUserOrElseThrow(userId);

        List<Event> events = eventRepository.findAllByInitiator(initiator, pageable);

        return EventMapper.toEventShortList(events, findEntity.findConfirmedRequestsMap(events));
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorAddEvent(Long userId, EventInputDto requestDto) {
        User initiator = findEntity.findUserOrElseThrow(userId);
        Category cat = findEntity.findCategoryOrElseThrow(requestDto.getCategoryId());
        Location loc = locationService.getLocationOrElseSave(requestDto.getLocation());

        findEntity.checkEventDate(requestDto.getEventDate());

        if (requestDto.getPaid() == null) requestDto.setPaid(false);
        if (requestDto.getRequestModeration() == null) requestDto.setRequestModeration(true);
        if (requestDto.getParticipantLimit() == null) requestDto.setParticipantLimit(0);

        Event event = EventMapper.toEvent(requestDto, initiator, cat, loc);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        event = eventRepository.save(event);

        return EventMapper.toOutputDto(event, List.of());
    }

    @Override
    public EventOutputFullDto initiatorGetEvent(Long userId, Long eventId) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);
        findEntity.checkEventInitiator(event, user);

        return EventMapper.toOutputDto(event, getRequestList(event));
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {

        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);

        findEntity.checkEventInitiator(event, user);
        findEntity.checkUnpublishedEvent(event);

        if (dto.getEventDate() != null) findEntity.checkEventDate(dto.getEventDate());

        if (dto.getStateAction() != null) userUpdateEventStatus(event, dto.getStateAction());

        Event updateEvent = updateEvent(event, dto);

        return EventMapper.toOutputDto(updateEvent, getRequestList(updateEvent));
    }

    @Override
    public List<EventOutputFullDto> findEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               String sort, int from, int size) {
        EventQueryCriteria criteria = EventQueryCriteria.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .build();

        List<BooleanExpression> conditions = new ArrayList<>();

        conditions.add(QEvent.event.state.eq(EventState.PUBLISHED));

        if (criteria.getText() != null) {
            conditions.add(QEvent.event.description.containsIgnoreCase(criteria.getText())
                    .or(QEvent.event.annotation.containsIgnoreCase(criteria.getText()))
                    .or(QEvent.event.title.containsIgnoreCase(criteria.getText())));
        }

        if (criteria.getPaid() != null) conditions.add(QEvent.event.paid.eq(criteria.getPaid()));

        if (criteria.getOnlyAvailable() != null && criteria.getOnlyAvailable()) {
            conditions.add(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.participantLimit.loe(findEntity.findAvailableEvents().size())));
        }

        fillCommonCriteria(criteria, conditions);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("невалидная дата");
            }
        }

        BooleanExpression finalCond = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Pageable pageable = PageableUtil.pageManager(from, size, null);
        List<Event> events = eventRepository.findAll(finalCond, pageable).getContent();

        return EventMapper.toEventFullDtoList(events, findEntity.findConfirmedRequestsMap(events));
    }

    @Override
    public EventOutputFullDto getEvent(Long id) {
        Event event = findEntity.findPublishedEventOrThrow(id);
        return EventMapper.toOutputDto(event, getRequestList(event));
    }

    private void adminUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case REJECT_EVENT:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new RequestNotProcessedException("Событие уже опубликовано или отменено");
                }
                event.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PUBLISHED) || event.getState().equals(EventState.CANCELED)) {
                    throw new RequestNotProcessedException("Событие уже опубликовано или отменено");
                }
                event.setState(EventState.PUBLISHED);
                break;
            default:
                findEntity.unsupportedStatus();
        }
    }

    private void userUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case CANCEL_REVIEW:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    throw new RequestNotProcessedException("Событие уже опубликовано или отменено");
                }
                event.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            default:
                findEntity.unsupportedStatus();
        }
    }

    private Event updateEvent(Event event, UpdateEventRequest dto) {
        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) event.setAnnotation(dto.getAnnotation());
        if (dto.getCategory() != null) {
            Category cat = findEntity.findCategoryOrElseThrow(dto.getCategory());
            event.setCategory(cat);
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank())
            event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getLocation() != null) {
            Location loc = locationService.getLocationOrElseSave(dto.getLocation());
            event.setLocation(loc);
        }
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) event.setTitle(dto.getTitle());

        return event;
    }

    private List<Request> getRequestList(Event event) {
        return findEntity.findConfirmedEventRequests(event);
    }

    private void fillCommonCriteria(EventQueryCriteria criteria, List<BooleanExpression> conditions) {
        if (criteria.getCategories() != null) {
            conditions.add(QEvent.event.category.id.in(criteria.getCategories()));
        }
/*
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isBefore(rangeEnd)) {
                throw new BadRequestException("невалидная дата");
            }
        }
*/
        LocalDateTime rangeStart;

        if (criteria.getRangeStart() == null) {
            rangeStart = LocalDateTime.now();
        } else {
            rangeStart = criteria.getRangeStart();
        }

        conditions.add(QEvent.event.eventDate.after(rangeStart));

        if (criteria.getRangeEnd() != null) {
            LocalDateTime rangeEnd = criteria.getRangeEnd();
            conditions.add(QEvent.event.eventDate.before(rangeEnd));
        }
    }
}
