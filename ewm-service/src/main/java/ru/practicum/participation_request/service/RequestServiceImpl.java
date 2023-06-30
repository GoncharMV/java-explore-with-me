package ru.practicum.participation_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.dto.RequestUpdateStatusResultDto;
import ru.practicum.participation_request.model.Request;
import ru.practicum.participation_request.repository.RequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.enums.RequestStatus;
import ru.practicum.utils.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final FindEntityUtilService findEntity;

    @Override
    public List<RequestDto> initiatorGetEventRequests(Long userId, Long eventId) {
        Event event = findEntity.findEventOrElseThrow(eventId);
        User user = findEntity.findUserOrElseThrow(userId);
        findEntity.checkEventInitiator(event, user);

        List<Request> requests = requestRepository.findRequestByEvent(event);

        return RequestMapper.toListDto(requests);
    }

    @Override
    @Transactional
    public RequestUpdateStatusResultDto initiatorChangeRequestStatus(Long userId,
                                                                     Long eventId,
                                                                     EventRequestUpdateDto updateDto) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);

        findEntity.checkEventInitiator(event, user);

        List<Request> requests = requestRepository.findRequestByIdIn(updateDto.getRequestIds());
        List<Request> confRequests = new ArrayList<>();
        List<Request> rejRequests = new ArrayList<>();

        for (Request r : requests) {
            r.setStatus(updateDto.getStatus());
            if (updateDto.getStatus().equals(RequestStatus.CONFIRMED)) {
                confRequests.add(r);
            } else if (updateDto.getStatus().equals(RequestStatus.REJECTED)) {
                rejRequests.add(r);
            }
        }

        return RequestUpdateStatusResultDto.builder()
                .confirmedRequests(RequestMapper.toListDto(confRequests))
                .rejectedRequests(RequestMapper.toListDto(rejRequests))
                .build();
    }

    @Override
    public List<RequestDto> participantGetRequests(Long userId) {
        User user = findEntity.findUserOrElseThrow(userId);
        List<Request> requests = requestRepository.findRequestByRequester(user);
        return RequestMapper.toListDto(requests);
    }

    @Override
    @Transactional
    public RequestDto participantAddRequest(Long userId, Long eventId) {
        User user = findEntity.findUserOrElseThrow(userId);
        Event event = findEntity.findEventOrElseThrow(eventId);

        Request newRequest = Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(RequestStatus.PENDING)
                .build();

        Request request = requestRepository.save(newRequest);

        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto participantCancelRequest(Long userId, Long requestId) {
        User user = findEntity.findUserOrElseThrow(userId);
        Request request = findEntity.findRequestOrElseThrow(requestId);
        findEntity.checkRequestRequestor(request, user);

        request.setStatus(RequestStatus.CANCELED);

        return RequestMapper.toRequestDto(request);
    }
}
