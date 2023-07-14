package ru.practicum.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.participation_request.model.Request;
import ru.practicum.participation_request.repository.RequestRepository;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.service.RatingService;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.RequestStatus;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class FindEntityUtilService {

    private final UserRepository userRepository;
    private final CategoriesRepository catRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final CompilationRepository compilationRepository;
    private final RatingService ratingService;

    public User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.USER + ConstantUtil.NOT_FOUND));
    }

    public Category findCategoryOrElseThrow(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.CAT + ConstantUtil.NOT_FOUND));
    }

    public Event findEventOrElseThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.EVENT + ConstantUtil.NOT_FOUND));
    }

    public Event findPublishedEventOrThrow(Long eventId) {
        return eventRepository.findByIdAndStateIs(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.EVENT + ConstantUtil.NOT_AVAILABLE));
    }

    public List<Event> findAvailableEvents() {
        List<Event> allEvents = eventRepository.findAll();
        Map<Event, List<Request>> requestByEvents = findConfirmedRequestsMap(allEvents);

        List<Event> availableEvents = new ArrayList<>();

        requestByEvents.forEach((event, requests) -> {
                    if (event.getParticipantLimit() > requests.size()) availableEvents.add(event);
                }
        );

        return availableEvents;
    }

    public Event checkEventPublished(Long eventId) {
        return eventRepository.findByIdAndStateIs(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new RequestNotProcessedException(ConstantUtil.EVENT + ConstantUtil.NOT_AVAILABLE));
    }

    public List<Event> findCategoryEvents(Category cat) {
        return eventRepository.findByCategory(cat);
    }

    public List<Event> findEventsByIds(List<Long> ids) {
        return eventRepository.findByIdIn(ids);
    }

    public Request findRequestOrElseThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.REQUEST + ConstantUtil.NOT_FOUND));
    }

    public List<Request> findConfirmedEventRequests(Event event) {
        return requestRepository.findRequestByEventAndStatusIs(event, RequestStatus.CONFIRMED);
    }

    public Map<Event, List<Request>> findConfirmedRequestsMap(List<Event> events) {
        Map<Event, List<Request>> confRequests = new HashMap<>();

        for (Event event : events) {
            List<Request> requests = findConfirmedEventRequests(event);
            confRequests.put(event, requests);
        }
        return confRequests;
    }

    public Compilation findCompilationOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(ConstantUtil.COMP + ConstantUtil.NOT_FOUND));
    }

    public void checkRepeatedRequest(Event event, User requester) {
        Request request = requestRepository.findByEventAndRequester(event, requester);

        if (request != null) {
            throw new RequestNotProcessedException(ConstantUtil.REQUEST + ConstantUtil.IS_EXISTS);
        }
    }

    public void checkIfLimitIsFull(Event event) {
        List<Request> requests = findConfirmedEventRequests(event);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == requests.size()) {
            throw new RequestNotProcessedException(ConstantUtil.REQUEST + ConstantUtil.REQ_LIMIT);
        }
    }

    public void checkEventInitiator(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new RequestNotProcessedException(ConstantUtil.USER + ConstantUtil.NO_ACCESS + ConstantUtil.EVENT);
        }
    }

    public void checkRequestInitiator(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new RequestNotProcessedException(ConstantUtil.CREATOR_REQ);
        }
    }

    public void checkUnpublishedEvent(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestNotProcessedException(ConstantUtil.EVENT + ConstantUtil.IS_FINAL);
        }
    }

    public void checkRequestRequestor(Request request, User user) {
        if (!request.getRequester().equals(user)) {
            throw new RequestNotProcessedException(ConstantUtil.ONLY_CREATOR);
        }
    }

    public void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException(ConstantUtil.DATA + ConstantUtil.NOT_AVAILABLE);
        }
    }

    public void unsupportedStatus() {
        throw new RequestNotProcessedException(ConstantUtil.EVENT + ConstantUtil.STATUS + ConstantUtil.NOT_AVAILABLE);
    }

    public void checkSearchRange(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
                throw new BadRequestException(ConstantUtil.DATA + ConstantUtil.NOT_AVAILABLE);
        }
    }

    public void checkUserEmailExists(String email) {
        User checkUser = userRepository.findByEmail(email);
        if (checkUser != null) {
            throw new RequestNotProcessedException(ConstantUtil.EMAIL_EXISTS);
        }
    }

    public void checkCatName(String name, Long catId) {
        Category checkCat = catRepository.findCategoryByName(name);

        if (checkCat != null && !checkCat.getId().equals(catId)) {
            throw new RequestNotProcessedException(ConstantUtil.CAT + ConstantUtil.IS_EXISTS);
        }
    }

    public void checkCatName(String name) {
        Category checkCat = catRepository.findCategoryByName(name);

        if (checkCat != null) {
            throw new RequestNotProcessedException(ConstantUtil.CAT + ConstantUtil.IS_EXISTS);
        }
    }

    public void thrNoAccess() {
        throw new RequestNotProcessedException(ConstantUtil.EVENT + ConstantUtil.IS_FINAL);
    }

    public Map<Event, EventRatingDto> findRatings(List<Event> events) {
        Map<Event, EventRatingDto> ratings = new HashMap<>();
        events.forEach(event -> ratings.put(event, ratingService.getRating(event.getId())));
        return ratings;
    }

}
