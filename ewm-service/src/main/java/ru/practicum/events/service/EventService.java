package ru.practicum.events.service;

import ru.practicum.events.dto.*;
import ru.practicum.utils.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventOutputFullDto adminUpdateEvent(Long eventId,
                                        UpdateEventAdminRequest requestDto);

    List<EventOutputFullDto> adminFindEvents(List<Long> users,
                                             List<EventState> states,
                                             List<Long> categories,
                                             LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd,
                                             int from,
                                             int size);

    List<EventShortDto> initiatorGetEvents(Long userId, int from, int size);

    EventOutputFullDto initiatorAddEvent(Long userId, EventInputDto requestDto);

    EventOutputFullDto initiatorGetEvent(Long userId, Long eventId);

    EventOutputFullDto initiatorUpdateEvent(Long userId, Long eventId, UpdateEventUserRequest requestDto);

    List<EventOutputFullDto> findEvents(String text,
                                        List<Long> categories,
                                        Boolean paid,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Boolean onlyAvailable,
                                        String sort,
                                        int from,
                                        int size, HttpServletRequest request);

    EventOutputFullDto getEvent(Long id, HttpServletRequest request);

    EventOutputFullDto addRating(Long userId, Long eventId, Boolean isLike);
}
