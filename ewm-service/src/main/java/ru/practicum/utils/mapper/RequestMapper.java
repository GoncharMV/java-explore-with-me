package ru.practicum.utils.mapper;

import ru.practicum.events.model.Event;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.model.Request;
import ru.practicum.users.model.User;

import java.util.ArrayList;
import java.util.List;

public final class RequestMapper {

    private RequestMapper() {
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static Request toRequest(RequestDto dto, User user, Event event) {
        return Request.builder()
                .id(dto.getId())
                .created(dto.getCreated())
                .event(event)
                .requester(user)
                .status(dto.getStatus())
                .build();
    }

    public static List<RequestDto> toListDto(List<Request> requests) {
        List<RequestDto> requestsDto = new ArrayList<>();
        for (Request r : requests) {
            requestsDto.add(toRequestDto(r));
        }
        return requestsDto;
    }
}
