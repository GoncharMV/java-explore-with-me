package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
import ru.practicum.utils.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.category.id IN ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5")
    List<Event> adminFindEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findByIdAndStateIs(Long eventId, EventState state);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = ru.practicum.utils.enums.EventState.PUBLISHED " +
            "AND (e.annotation LIKE CONCAT('%',?1,'%') OR e.description LIKE CONCAT('%',?1,'%')) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid = ?3 " +
            "AND e.eventDate BETWEEN ?4 AND ?5 " +
            "AND ((?6 = true AND e.participantLimit = 0) " +
            "OR (?6 = false))")
    List<Event> findPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);
}
