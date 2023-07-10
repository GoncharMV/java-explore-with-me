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
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * admin API for work with events
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    /**
     * admin's search for events with certain parameters
     *
     * @param users list of creator's ids in which the search is made
     * @param states list of event states in which the search is made
     * @param categories list of category's ids in which the search is made
     * @param rangeStart start date from which the search is made
     * @param rangeEnd end date
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of events with all available parameters, or empty list if nothing is found
     * @throws BadRequestException if the start date is after the end date
     */
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

    /**
     * Updates event, publish or reject publication
     *
     * @param eventId event id
     * @param requestDto update event dto
     * @return data object with all parameters
     * @throws ObjectNotFoundException if event does not exist
     * @throws RequestNotProcessedException if tries to change published or canceled event's status
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto adminUpdateEvent(@PathVariable Long eventId,
                                               @RequestBody @Valid UpdateEventAdminRequest requestDto) {
        log.info("Событие отредактировано");
        return eventService.adminUpdateEvent(eventId, requestDto);
    }
}
