package ru.practicum.api.private_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.participation_request.dto.RequestDto;
import ru.practicum.participation_request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> participantGetRequests(@PathVariable Long userId) {
        log.info("Найдены запросы на участие");
        return requestService.participantGetRequests(userId);
    }

    @PostMapping
    public RequestDto participantAddRequest(@PathVariable Long userId,
                                            @RequestParam Long eventId) {
        log.info("Заявка создана");
        return requestService.participantAddRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto participantCancelRequest(@PathVariable Long userId,
                                               @PathVariable Long requestId) {
        log.info("Заявка отменена");
        return requestService.participantCancelRequest(userId, requestId);
    }

}
