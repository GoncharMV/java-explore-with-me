package ru.practicum.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventQueryCriteria;
import ru.practicum.events.dto.UpdateEventRequest;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.QEvent;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.service.LocationService;
import ru.practicum.utils.CheckUtilService;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.enums.EventSort;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.StateAction;
import ru.practicum.utils.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventServiceUtil {

    private final FindEntityUtilService findEntity;
    private final CheckUtilService checkEntity;
    private final LocationService locationService;

    public Pageable fillPageable(EventSort sort, int from, int size) {
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

    public void adminUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case REJECT_EVENT:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    checkEntity.thrNoAccess();
                }
                event.setState(EventState.CANCELED);
                break;
            case PUBLISH_EVENT:
                if (event.getState().equals(EventState.PUBLISHED)
                        || event.getState().equals(EventState.CANCELED)) {
                    checkEntity.thrNoAccess();
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            default:
                checkEntity.unsupportedStatus();
        }
    }

    public void userUpdateEventStatus(Event event, StateAction stateAction) {
        switch (stateAction) {
            case CANCEL_REVIEW:
                if (event.getState().equals(EventState.PUBLISHED)) {
                    checkEntity.thrNoAccess();
                }
                event.setState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setState(EventState.PENDING);
                break;
            default:
                checkEntity.unsupportedStatus();
        }
    }

    public Event updateEvent(Event event, UpdateEventRequest dto) {
        setCatIfNotNull(event, dto.getCategory());
        setLocIfNotNull(event, dto.getLocation());
        fillAnnotation(dto.getAnnotation(), event);
        fillDescription(dto.getDescription(), event);
        fillEventDate(dto.getEventDate(), event);
        fillPaid(dto.getPaid(), event);
        fillPartLimit(dto.getParticipantLimit(), event);
        fillModeration(dto.getRequestModeration(), event);
        fillTitle(dto.getTitle(), event);

        return event;
    }

    private void fillTitle(String title, Event event) {
        if (title != null && !title.isBlank()) {
            event.setTitle(title);
        }
    }

    private void fillModeration(Boolean requestModeration, Event event) {
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
    }

    private void fillPartLimit(Integer participantLimit, Event event) {
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
    }

    private void fillPaid(Boolean paid, Event event) {
        if (paid != null) {
            event.setPaid(paid);
        }
    }

    private void fillEventDate(LocalDateTime eventDate, Event event) {
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
    }

    private void fillDescription(String description, Event event) {
        if (description != null && !description.isBlank()) {
            event.setDescription(description);
        }
    }

    private void fillAnnotation(String annotation, Event event) {
        if (annotation != null && !annotation.isBlank()) {
            event.setAnnotation(annotation);
        }
    }

    public void setLocIfNotNull(Event event, LocationDto location) {
        if (location != null) {
            Location loc = locationService.getLocationOrElseSave(location);
            event.setLocation(loc);
        }
    }

    public void setCatIfNotNull(Event event, Long category) {
        if (category != null) {
            Category cat = findEntity.findCategoryOrElseThrow(category);
            event.setCategory(cat);
        }
    }

    public void fillCommonCriteria(EventQueryCriteria criteria, List<BooleanExpression> conditions) {
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

}
