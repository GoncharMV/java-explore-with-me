package ru.practicum.participation_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.utils.FindEntityUtilService;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final FindEntityUtilService findEntity;

    @Override
    public RequestDto initiatorGetEventRequests(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public RequestDto initiatorChangeRequestStatus(Long userId, Long eventId, EventRequestUpdateDto updateDto) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);



        return null;
    }

    @Override
    public List<RequestDto> participantGetRequests(Long userId) {
        return null;
    }

    @Override
    @Transactional
    public RequestDto participantAddRequest(Long userId, Long eventId) {
        return null;
    }

    @Override
    @Transactional
    public RequestDto participantCancelRequest(Long userId, Long requestId) {
        return null;
    }
}
