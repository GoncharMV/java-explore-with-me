package ru.practicum.participation_request.service;

import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.dto.RequestUpdateStatusResultDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> initiatorGetEventRequests(Long userId, Long eventId);

    RequestUpdateStatusResultDto initiatorChangeRequestStatus(Long userId,
                                                              Long eventId,
                                                              EventRequestUpdateDto updateDto);

    List<RequestDto> participantGetRequests(Long userId);

    RequestDto participantAddRequest(Long userId, Long eventId);

    RequestDto participantCancelRequest(Long userId, Long requestId);
}
