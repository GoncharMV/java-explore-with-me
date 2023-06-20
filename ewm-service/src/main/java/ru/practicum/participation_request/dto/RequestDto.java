package ru.practicum.participation_request.dto;

import ru.practicum.utils.RequestStatus;

import java.time.LocalDateTime;

public class RequestDto {

    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private RequestStatus status;

}
