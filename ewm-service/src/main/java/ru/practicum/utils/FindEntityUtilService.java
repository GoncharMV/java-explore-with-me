package ru.practicum.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.participation_request.model.Request;
import ru.practicum.participation_request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.RequestStatus;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class FindEntityUtilService {

    private final UserRepository userRepository;
    private final CategoriesRepository catRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

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

    public Event findPublishedEventOrElseThrow(Long eventId) {
        return eventRepository.findByIdAndStateIs(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new ObjectNotFoundException("Мероприятие не найдено или недоступно"));
    }

    public List<Request> findEventRequests(Event event) {
        return requestRepository.findRequestByEventIdAndStatusIs(event.getId(), RequestStatus.CONFIRMED);
    }

    public void checkEventInitiator(Event event, Long userId) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new RequestNotProcessedException("Доступ к функции есть только у создателя мероприятия");
        }
    }

    public void checkUnpublishedEvent(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestNotProcessedException("Редактировать можно толкьо неопубликованные события");
        }
    }

    public void unsupportedStatus() {
        throw new RequestNotProcessedException("Статус не поддерживается");
    }
}
