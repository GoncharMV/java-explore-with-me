package ru.practicum.participation_request.model;

import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;
import ru.practicum.utils.enums.RequestStatus;

import java.time.LocalDateTime;

public class Request {

    private Long id;
    private LocalDateTime created;
    private Event event;
    private User requester;
    private RequestStatus status;

}
