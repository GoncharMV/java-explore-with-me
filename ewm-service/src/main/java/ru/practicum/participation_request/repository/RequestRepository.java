package ru.practicum.participation_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.participation_request.model.Request;
import ru.practicum.users.model.User;
import ru.practicum.utils.enums.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestByEventAndStatusIs(Event event, RequestStatus status);

    List<Request> findRequestByEvent(Event event);

    List<Request> findRequestByRequester(User user);

    List<Request> findRequestByIdIn(List<Long> ids);

    Request findByEventAndRequester(Event event, User requester);
}
