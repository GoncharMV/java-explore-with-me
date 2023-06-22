package ru.practicum.events.service;

import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputDto;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;

import java.util.List;

public interface EventService {
    EventOutputDto adminUpdateEvent(Long eventId,
                                    EventInputDto requestDto);

    List<EventOutputDto> adminFindEvents(List<Integer> users,
                                         List<String> states,
                                         List<Integer> categories,
                                         String rangeStart,
                                         String rangeEnd,
                                         int from,
                                         int size);

    EventOutputDto initiatorGetEvents(Long userId, int from, int size);

    EventOutputDto initiatorAddEvent(Long userId, EventInputDto requestDto);

    EventOutputDto initiatorGetEvent(Long userId, Long eventId);

    EventOutputDto initiatorUpdateEvent(Long userId, Long eventId, EventInputDto requestDto);

    List<EventOutputDto> findEvents(String text,
                                    List<Integer> categories,
                                    Boolean paid,
                                    String rangeStart,
                                    String rangeEnd,
                                    Boolean onlyAvailable,
                                    String sort,
                                    int from,
                                    int size);

    EventOutputDto getEvent(Long id);
}
