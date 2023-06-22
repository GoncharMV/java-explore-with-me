package ru.practicum.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputDto;
import ru.practicum.events.repository.EventRepository;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EventOutputDto adminUpdateEvent(Long eventId, EventInputDto requestDto) {
        return null;
    }

    @Override
    public List<EventOutputDto> adminFindEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                                String rangeStart, String rangeEnd, int from, int size) {
        return null;
    }

    @Override
    public EventOutputDto initiatorGetEvents(Long userId, int from, int size) {
        return null;
    }

    @Override
    @Transactional
    public EventOutputDto initiatorAddEvent(Long userId, EventInputDto requestDto) {
        return null;
    }

    @Override
    public EventOutputDto initiatorGetEvent(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventOutputDto initiatorUpdateEvent(Long userId, Long eventId, EventInputDto requestDto) {
        return null;
    }

    @Override
    public RequestDto initiatorGetEventRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public EventRequestUpdateDto initiatorChangeRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestUpdateDto updateDto) {
        return null;
    }

    @Override
    public List<EventOutputDto> findEvents(String text, List<Integer> categories, Boolean paid,
                                           String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                           String sort, int from, int size) {
        return null;
    }

    @Override
    public EventOutputDto getEvent(Long id) {
        return null;
    }
}
