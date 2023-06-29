package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutputFullDto;
import ru.practicum.events.dto.UpdateEventAdminRequest;
import ru.practicum.events.service.EventService;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventOutputFullDto> adminFindEvents(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = ConstantUtil.DATA_FORMAT)
            LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = ConstantUtil.DATA_FORMAT)
            LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("События найдены");
        return eventService.adminFindEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto adminUpdateEvent(@PathVariable Long eventId,
                                               @RequestBody UpdateEventAdminRequest requestDto) {
        log.info("Событие отредактировано");
        return eventService.adminUpdateEvent(eventId, requestDto);
    }


}
