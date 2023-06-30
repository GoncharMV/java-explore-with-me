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
import ru.practicum.utils.exception.RequestNotProcessedException;
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

        RequestStatus status = updateDto.getStatus();

        for (Request r : requests) {
            if (r.getStatus().equals(RequestStatus.CONFIRMED)) {
                throw new RequestNotProcessedException("Невозможно изменить статус принятой заявки");
            }

            if (status.equals(RequestStatus.REJECTED)) {
                r.setStatus(status);
                rejRequests.add(r);
            }

            Integer limit = event.getParticipantLimit();

            if (status.equals(RequestStatus.CONFIRMED)) {
                if (limit == 0) {
                    r.setStatus(status);
                    confRequests.add(r);
                }

                if (confRequests.size() < limit) {
                    r.setStatus(status);
                    confRequests.add(r);
                } else if (confRequests.size() == limit) {
                    throw new RequestNotProcessedException("Лимит заявок исчерпан");
                }
            }
        }

        RequestUpdateStatusResultDto result = new RequestUpdateStatusResultDto();
        result.setConfirmedRequests(RequestMapper.toListDto(confRequests));
        result.setRejectedRequests(RequestMapper.toListDto(rejRequests));
        return result;
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
        Event event = findEntity.findPublishedEventOrElseThrow(eventId);

        findEntity.checkRequestInitiator(event, user);

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

        if (!request.getStatus().equals(RequestStatus.CONFIRMED)) {
            request.setStatus(RequestStatus.CANCELED);
        } else {
            throw new RequestNotProcessedException("Нельзя отменить принятую заявку");
        }

        return RequestMapper.toRequestDto(request);
    }
}
