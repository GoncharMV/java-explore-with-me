package ru.practicum.api.private_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.service.RequestService;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import java.util.List;

/**
 * private API for work with participation requests
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;

    /**
     * Get current user's requests
     *
     * @param userId user id
     * @return list of current user's requests
     * @throws ObjectNotFoundException if user does not exist
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RequestDto> participantGetRequests(@PathVariable Long userId) {
        log.info("Найдены запросы на участие");
        return requestService.participantGetRequests(userId);
    }

    /**
     * Create participation request for specific event
     *
     * @param userId user id
     * @param eventId event id
     * @return request dto with pending status
     * @throws ObjectNotFoundException if user or event does not exist
     * @throws RequestNotProcessedException if event's creator tries to participate
     * @throws RequestNotProcessedException if current user already applied to participate
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto participantAddRequest(@PathVariable Long userId,
                                            @RequestParam Long eventId) {
        log.info("Заявка создана");
        return requestService.participantAddRequest(userId, eventId);
    }

    /**
     * Cancel your own participation request
     *
     * @param userId user id
     * @param requestId request id
     * @return request dto with canceled status
     * @throws ObjectNotFoundException if user or request does not exist
     * @throws RequestNotProcessedException if tries to cancel confirmed request
     */
    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public RequestDto participantCancelRequest(@PathVariable Long userId,
                                               @PathVariable Long requestId) {
        log.info("Заявка отменена");
        return requestService.participantCancelRequest(userId, requestId);
    }

}
