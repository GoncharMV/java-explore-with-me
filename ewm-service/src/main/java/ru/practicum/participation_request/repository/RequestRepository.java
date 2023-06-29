package ru.practicum.participation_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.participation_request.model.Request;
import ru.practicum.utils.enums.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findRequestByEventIdAndStatusIs(Long eventId, RequestStatus status);
}
