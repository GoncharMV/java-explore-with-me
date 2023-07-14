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
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.participation_request.model.Request;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.service.RatingService;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;
import ru.practicum.users.service.UserService;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.enums.EventSort;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.stats.StatsService;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.utils.mapper.EventMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final FindEntityUtilService findEntity;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final StatsService statsService;
    private final RatingService ratingService;
    private final UserService userService;


    @Override
    @Transactional
    public EventOutputFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest dto) {
        Event event = findEntity.findEventOrElseThrow(eventId);

        if (dto.getEventDate() != null) {
            findEntity.checkEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() != null) {
            adminUpdateEventStatus(event, dto.getStateAction());
        }

        Event updateEvent = updateEvent(event, dto);

        return toFullDto(updateEvent);
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
        findEntity.checkSearchRange(rangeStart, rangeEnd);
        BooleanExpression finalCond = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        List<Event> events = eventRepository.findAll(finalCond, pageable).getContent();

        return toFullDtoList(events);
    }

    @Override
    public List<EventShortDto> initiatorGetEvents(Long userId, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);
        User initiator = findEntity.findUserOrElseThrow(userId);

        List<Event> events = eventRepository.findAllByInitiator(initiator, pageable);

        return toShortDtoList(events);
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorAddEvent(Long userId, EventInputDto requestDto) {
        User initiator = findEntity.findUserOrElseThrow(userId);
        Category cat = findEntity.findCategoryOrElseThrow(requestDto.getCategoryId());
        Location loc = locationService.getLocationOrElseSave(requestDto.getLocation());

        findEntity.checkEventDate(requestDto.getEventDate());

        if (requestDto.getPaid() == null) {
            requestDto.setPaid(false);
        }
        if (requestDto.getRequestModeration() == null) {
            requestDto.setRequestModeration(true);
        }
        if (requestDto.getParticipantLimit() == null) {
            requestDto.setParticipantLimit(0);
        }

        Event event = EventMapper.toEvent(requestDto, initiator, cat, loc);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        event = eventRepository.save(event);
        event.setRating(findEntity.getAvgRating(event));

        return toFullDto(event);
    }

    @Override
    public EventOutputFullDto initiatorGetEvent(Long userId, Long eventId) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);
        findEntity.checkEventInitiator(event, user);

        return toFullDto(event);
    }

    @Override
    @Transactional
    public EventOutputFullDto initiatorUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest dto) {

        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);

        findEntity.checkEventInitiator(event, user);
        findEntity.checkUnpublishedEvent(event);

        if (dto.getEventDate() != null) {
            findEntity.checkEventDate(dto.getEventDate());
        }

        if (dto.getStateAction() != null) {
            userUpdateEventStatus(event, dto.getStateAction());
        }

        Event updateEvent = updateEvent(event, dto);

        return toFullDto(updateEvent);
    }

    @Override
    public List<EventOutputFullDto> findEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               EventSort sort, int from, int size, HttpServletRequest request) {
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

        if (criteria.getPaid() != null) {
            conditions.add(QEvent.event.paid.eq(criteria.getPaid()));
        }

        if (criteria.getOnlyAvailable() != null && criteria.getOnlyAvailable()) {
            conditions.add(QEvent.event.participantLimit.eq(0)
                    .or(QEvent.event.participantLimit.loe(findEntity.findAvailableEvents().size())));
        }

        fillCommonCriteria(criteria, conditions);
        findEntity.checkSearchRange(rangeStart, rangeEnd);

        BooleanExpression finalCond = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Pageable pageable = PageableUtil.pageManager(from, size, null);
        if (sort != null) {
            pageable = fillPageable(sort, from, size);
        }

        List<Event> events = eventRepository.findAll(finalCond, pageable).getContent();

        statsService.addHit(request);

        return toFullDtoList(events);
    }

    @Override
    public EventOutputFullDto getEvent(Long id, HttpServletRequest request) {
        Event event = findEntity.findPublishedEventOrThrow(id);

        statsService.addHit(request);

        return toFullDto(event);
    }

    /**
     * method for adding rating to the event. Average rating calculates as a subtraction dislikes from likes
     *
     * @param isLike = true, set like; isLike = false, set dislike
     */
    @Override
    @Transactional
    public EventOutputFullDto addRating(Long userId, Long eventId, Boolean isLike) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);

        findEntity.checkIfInitiatorThrow(event, user);
        findEntity.checkParticipation(event, user);

        if (isLike) {
            ratingService.addLike(userId, eventId);
        } else {
            ratingService.addDislike(userId, eventId);
        }
        event.setRating(findEntity.getAvgRating(event));

        return toFullDto(event);
    }

    private Pageable fillPageable(EventSort sort, int from, int size) {
        Pageable pageable;
        switch (sort) {
            case VIEWS:
                pageable = PageableUtil.pageManager(from, size, ConstantUtil.VIEWS);
                break;
            case EVENT_DATE:
                pageable = PageableUtil.pageManager(from, size, ConstantUtil.EVENT_DATE);
                break;
            case RATING:
                pageable = PageableUtil.pageManager(from, size, ConstantUtil.RATING_STR);
                break;
            default:
                throw new BadRequestException(ConstantUtil.STATUS + ConstantUtil.NOT_AVAILABLE);
        }
        return pageable;
    }

    private void adminUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case REJECT_EVENT:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    findEntity.thrNoAccess();
                }
                event.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PUBLISHED)
                        || event.getState().equals(EventState.CANCELED)) {
                    findEntity.thrNoAccess();
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            default:
                findEntity.unsupportedStatus();
        }
    }

    private void userUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case CANCEL_REVIEW:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    findEntity.thrNoAccess();
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
        setCatIfNotNull(event, dto.getCategory());
        setLocIfNotNull(event, dto.getLocation());

        if (dto.getAnnotation() != null && !dto.getAnnotation().isBlank()) {
            event.setAnnotation(dto.getAnnotation());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            event.setDescription(dto.getDescription());
        }

        if (dto.getEventDate() != null) {
            event.setEventDate(dto.getEventDate());
        }

        if (dto.getPaid() != null) {
            event.setPaid(dto.getPaid());
        }
        if (dto.getParticipantLimit() != null) {
            event.setParticipantLimit(dto.getParticipantLimit());
        }
        if (dto.getRequestModeration() != null) {
            event.setRequestModeration(dto.getRequestModeration());
        }

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            event.setTitle(dto.getTitle());
        }

        return event;
    }

    private void setLocIfNotNull(Event event, LocationDto location) {
        if (location != null) {
            Location loc = locationService.getLocationOrElseSave(location);
            event.setLocation(loc);
        }
    }

    private void setCatIfNotNull(Event event, Long category) {
        if (category != null) {
            Category cat = findEntity.findCategoryOrElseThrow(category);
            event.setCategory(cat);
        }
    }

    private void fillCommonCriteria(EventQueryCriteria criteria, List<BooleanExpression> conditions) {
        if (criteria.getCategories() != null) {
            conditions.add(QEvent.event.category.id.in(criteria.getCategories()));
        }

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

    private List<EventOutputFullDto> toFullDtoList(List<Event> events) {
        Map<Long, Long> views = statsService.getViews(events);
        events.forEach(event -> event.setViews(views.getOrDefault(event.getId(), 0L)));
        Map<Event, List<Request>> requests = findEntity.findConfirmedRequestsMap(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);
        Map<Event, UserPublicDto> users = userService.findUsersEvents(events);

        return EventMapper.toEventFullDtoList(events, users, requests, views, ratings);
    }

    @Override
    public List<EventShortDto> toShortDtoList(List<Event> events) {
        Map<Long, Long> views = statsService.getViews(events);
        Map<Event, List<Request>> requests = findEntity.findConfirmedRequestsMap(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);
        Map<Event, UserPublicDto> users = userService.findUsersEvents(events);

        return EventMapper.toEventShortList(events, users, requests, views, ratings);
    }

    private EventOutputFullDto toFullDto(Event event) {
        return toFullDtoList(List.of(event)).get(0);
    }
}
