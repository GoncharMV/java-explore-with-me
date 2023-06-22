package ru.practicum.api.private_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputDto;
import ru.practicum.events.service.EventService;
import ru.practicum.participation_request.dto.EventRequestUpdateDto;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.service.RequestService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public EventOutputDto initiatorGetEvents(@PathVariable Long userId,
                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("События найдены");
        return eventService.initiatorGetEvents(userId, from, size);
    }

    @PostMapping
    public EventOutputDto initiatorAddEvent(@PathVariable Long userId,
                                            @RequestBody @Valid EventInputDto requestDto) {
        log.info("Событие добавлено");
        return eventService.initiatorAddEvent(userId, requestDto);
    }

    @GetMapping("/{eventId}")
    public EventOutputDto initiatorGetEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Событие найдено");
        return eventService.initiatorGetEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventOutputDto initiatorUpdateEvent(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @RequestBody EventInputDto requestDto) {
        log.info("Событие обновлено");
        return eventService.initiatorUpdateEvent(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public RequestDto initiatorGetEventRequests(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("Найдены запросы на участие");
        return requestService.initiatorGetEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestDto initiatorChangeRequestStatus(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestBody EventRequestUpdateDto updateDto) {
        log.info("Статус заявок изменён");
        return requestService.initiatorChangeRequestStatus(userId, eventId, updateDto);
    }

}
