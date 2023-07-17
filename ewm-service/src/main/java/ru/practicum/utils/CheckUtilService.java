package ru.practicum.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.model.Event;
import ru.practicum.participation_request.model.Request;
import ru.practicum.participation_request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CheckUtilService {

    private final UserRepository userRepository;
    private final CategoriesRepository catRepository;
    private final RequestRepository requestRepository;
    private final FindEntityUtilService findEntity;

    public void checkRepeatedRequest(Event event, User requester) {
        Request request = requestRepository.findByEventAndRequester(event, requester);

        if (request != null) {
            throw new RequestNotProcessedException(ConstantUtil.REQUEST + ConstantUtil.IS_EXISTS);
        }
    }

    public void checkParticipation(Event event, User user) {
        Request request = requestRepository.findByEventAndRequester(event, user);

        if (request == null) {
            throw new RequestNotProcessedException(ConstantUtil.RATING + ConstantUtil.NOT_AVAILABLE);
        }
    }

    public void checkIfLimitIsFull(Event event) {
        List<Request> requests = findEntity.findConfirmedEventRequests(event);

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

    public void checkIfInitiatorThrow(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new RequestNotProcessedException(ConstantUtil.CREATOR_RATING);
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

}
