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
import ru.practicum.participation_request.dto.RequestUpdateStatusResultDto;
import ru.practicum.participation_request.service.RequestService;
import ru.practicum.utils.exception.BadRequestException;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import javax.validation.Valid;
import java.util.List;

/**
 * private API for work with events
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;
    private final RequestService requestService;

    /**
     * Allows creator to see the list of their events
     *
     * @param userId creator id
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of events with short parameters
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> initiatorGetEvents(@PathVariable Long userId,
                                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("События найдены");
        return eventService.initiatorGetEvents(userId, from, size);
    }

    /**
     * Create new event and send to administration for approval
     *
     * @param userId creator id
     * @param requestDto event's parameters
     * @return full event with available parameters
     * @throws ObjectNotFoundException if user or category does not exist
     * @throws BadRequestException if event's date is less than hour away
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventOutputFullDto initiatorAddEvent(@PathVariable Long userId,
                                                @RequestBody @Valid EventInputDto requestDto) {
        log.info("Событие добавлено");
        return eventService.initiatorAddEvent(userId, requestDto);
    }

    /**
     * Allows creator to the info of event
     *
     * @param userId  creator id
     * @param eventId event id
     * @return full event dto with all parameters
     * @throws ObjectNotFoundException if user or event does not exist
     */
    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto initiatorGetEvent(@PathVariable Long userId,
                                                @PathVariable Long eventId) {
        log.info("Событие найдено");
        return eventService.initiatorGetEvent(userId, eventId);
    }

    /**
     * Updates event by creator
     *
     * @param userId creator id
     * @param eventId event id
     * @param requestDto update event dto
     * @return updated data object with all parameters
     * @throws ObjectNotFoundException if user or event does not exist
     * @throws RequestNotProcessedException if event is published, cannot change published events
     * @throws BadRequestException if event's date is less than hour away
     */
    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto initiatorUpdateEvent(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestBody @Valid UpdateEventUserRequest requestDto) {
        log.info("Событие обновлено");
        return eventService.initiatorUpdateEvent(userId, eventId, requestDto);
    }

    /**
     * Allows creator to see all participation request's on particular event
     *
     * @param userId creator id
     * @param eventId event id
     * @return list of participation requests for specific event
     * @throws ObjectNotFoundException if user or event does not exist
     * @throws RequestNotProcessedException if user in userId is not the one who created event
     */
    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> initiatorGetEventRequests(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        log.info("Найдены запросы на участие");
        return requestService.initiatorGetEventRequests(userId, eventId);
    }

    /**
     * Allows creator to confirm or reject participation requests in bulk
     *
     * @param userId creator id
     * @param eventId event id
     * @param updateDto list of request's ids and status to what to change
     * @return lists of confirmed and rejected requests for specific event
     * @throws ObjectNotFoundException if user or event does not exist
     * @throws RequestNotProcessedException if user in userId is not the one who created event
     * @throws RequestNotProcessedException if participation limit is reached
     * @throws RequestNotProcessedException if tries to reject confirmed request
     */
    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public RequestUpdateStatusResultDto initiatorChangeRequestStatus(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestUpdateDto updateDto) {
        log.info("Статус заявок изменён");
        return requestService.initiatorChangeRequestStatus(userId, eventId, updateDto);
    }

    /**
     * add like to the event
     *
     * @param userId user id
     * @param eventId event id
     * @return event full parameters
     * @throws RequestNotProcessedException if initiator or non-participant tries to leave the like
     */
    @PostMapping("/{eventId}/like")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto addLike(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Добавлен лайк мероприятию");
        return eventService.addRating(userId, eventId, true);
    }

    /**
     * add dislike to the event
     *
     * @param userId user id
     * @param eventId event id
     * @return event full parameters
     * @throws RequestNotProcessedException if initiator or non-participant tries to leave the dislike
     */
    @PostMapping("/{eventId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public EventOutputFullDto addDislike(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Добавлен дизлайк мероприятию");
        return eventService.addRating(userId, eventId, false);
    }

}
