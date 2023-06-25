package ru.practicum.api.private_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.UpdateEventUserRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> initiatorGetEvents(@PathVariable Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("События найдены");
        return eventService.initiatorGetEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutputFullDto initiatorAddEvent(@PathVariable Long userId,
                                                @RequestBody @Valid EventInputDto requestDto) {
        log.info("Событие добавлено");
        return eventService.initiatorAddEvent(userId, requestDto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto initiatorGetEvent(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("Событие найдено");
        return eventService.initiatorGetEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto initiatorUpdateEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestBody UpdateEventUserRequest requestDto) {
        log.info("Событие обновлено");
        return eventService.initiatorUpdateEvent(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto initiatorGetEventRequests(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("Найдены запросы на участие");
        return requestService.initiatorGetEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto initiatorChangeRequestStatus(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestBody EventRequestUpdateDto updateDto) {
        log.info("Статус заявок изменён");
        return requestService.initiatorChangeRequestStatus(userId, eventId, updateDto);
    }

}
