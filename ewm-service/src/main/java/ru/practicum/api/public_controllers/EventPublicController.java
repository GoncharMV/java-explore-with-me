package ru.practicum.api.public_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutputFullDto;
import ru.practicum.events.service.EventService;
import ru.practicum.utils.ConstantUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;
    private final HttpServletRequest request;

    @GetMapping
    public List<EventOutputFullDto> findEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false)  @DateTimeFormat(pattern = ConstantUtil.DATA_FORMAT)
            LocalDateTime rangeStart,
            @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = ConstantUtil.DATA_FORMAT)
            LocalDateTime rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("События найдены");
        return eventService.findEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public EventOutputFullDto getEvent(@PathVariable Long id) {
        log.info("Событие найдено");
        return eventService.getEvent(id, request.getRemoteAddr());
    }

}
