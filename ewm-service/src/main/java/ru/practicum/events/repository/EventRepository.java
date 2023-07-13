package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Category;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
import ru.practicum.utils.enums.EventState;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findByCategory(Category category);

    List<Event> findAllByInitiator(User initiator, Pageable pageable);

    Optional<Event> findByIdAndStateIs(Long eventId, EventState state);

    List<Event> findByIdIn(List<Long> ids);
}
