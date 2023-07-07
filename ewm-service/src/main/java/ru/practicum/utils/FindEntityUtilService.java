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

    public User findUserOrElseThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
    }

    public Category findCategoryOrElseThrow(Long catId) {
        return catRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена"));
    }

    public Event findEventOrElseThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Мероприятие не найдено"));
    }

    public Event findPublishedEventOrThrow(Long eventId) {
        return eventRepository.findByIdAndStateIs(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException("Мероприятие не найдено или недоступно"));
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
                .orElseThrow(() -> new RequestNotProcessedException("Мероприятие недоступно"));
    }

    public List<Event> findCategoryEvents(Category cat) {
        return eventRepository.findByCategory(cat);
    }

    public List<Event> findEventsByIds(List<Long> ids) {
        return eventRepository.findByIdIn(ids);
    }

    public Request findRequestOrElseThrow(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Заявка на участие не найдена"));
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
                .orElseThrow(() -> new ObjectNotFoundException("Подборка на участие не найдена"));
    }

    public void checkRepeatedRequest(Event event, User requester) {
        Request request = requestRepository.findByEventAndRequester(event, requester);

        if (request != null) {
            throw new RequestNotProcessedException("Заявка на участие уже подана");
        }
    }

    public void checkIfLimitIsFull(Event event) {
        List<Request> requests = findConfirmedEventRequests(event);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == requests.size()) {
            throw new RequestNotProcessedException("Лимит заявок исчерпан");
        }
    }

    public void checkEventInitiator(Event event, User user) {
        if (!event.getInitiator().equals(user)) {
            throw new RequestNotProcessedException("Доступ к функции есть только у создателя мероприятия");
        }
    }

    public void checkRequestInitiator(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new RequestNotProcessedException("Создатель мероприятия не может подавать заявку на участие");
        }
    }

    public void checkUnpublishedEvent(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestNotProcessedException("Редактировать можно толкьо неопубликованные события");
        }
    }

    public void checkRequestRequestor(Request request, User user) {
        if (!request.getRequester().equals(user)) {
            throw new RequestNotProcessedException("Доступ к функции есть только у создателя заявки");
        }
    }

    public void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Невалидная дата события");
        }
    }

    public void unsupportedStatus() {
        throw new RequestNotProcessedException("Статус не поддерживается");
    }

    public void checkSearchRange(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            if (start.isAfter(end)) {
                throw new BadRequestException("невалидная дата");
            }
        }
    }

    public void thrNoAccess() {
        throw new RequestNotProcessedException("Событие уже опубликовано или отменено");
    }

}
