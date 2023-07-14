package ru.practicum.api.public_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventOutputFullDto;
import ru.practicum.events.service.EventService;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.enums.EventSort;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * public API for work with events
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    /**
     * public search for events with certain parameters
     *
     * @param text for search in title, description or annotation of events
     * @param categories list of category ids in which the search is made
     * @param paid = false - event is free of charge, paid = true - event costs money
     * @param rangeStart start date from which the search is made
     * @param rangeEnd end date
     * @param onlyAvailable = true - events without participation limit or with available spots
     * @param sort by VIEW, EVENT_DATA or RATING
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of events with all available parameters, or empty list if nothing is found
     * @throws BadRequestException if the start date is after the end date
     */
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
            @RequestParam(name = "sort", required = false) EventSort sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request) {
        log.info("События найдены");
        return eventService.findEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request);
    }

    /**
     *
     * @param id event id
     * @param request servlet request
     * @return full info of specific event
     * @throws ObjectNotFoundException if event is unpublished or does not exist
     */
    @GetMapping("/{id}")
    public EventOutputFullDto getEvent(@PathVariable Long id,
                                       HttpServletRequest request) {
        log.info("Событие найдено");
        return eventService.getEvent(id, request);
    }

}
