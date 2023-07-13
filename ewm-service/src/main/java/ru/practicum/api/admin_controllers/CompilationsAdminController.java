package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.dto.CompilationUpdateDto;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.utils.exception.ObjectNotFoundException;

import javax.validation.Valid;

/**
 * admin API for work with event's compilations
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsAdminController {

    private final CompilationService compilationService;

    /**
     *  Save new compilation with list of events
     *  Choose to pin it to the top of the home page (default value = false)
     * @param requestDto new comp dto, title must not be blank
     * @return saved compilation with list of events
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationOutputDto adminAddCompilation(@RequestBody @Valid CompilationInputDto requestDto) {
        log.info("Подборка добавлена");
        return compilationService.adminAddCompilation(requestDto);
    }

    /**
     * Delete existing compilation
     * @param compId compilation id
     * @throws ObjectNotFoundException if comp does not exist
     */
    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminRemoveCompilation(@PathVariable Long compId) {
        log.info("Подборка удалена");
        compilationService.adminRemoveCompilation(compId);
    }

    /**
     * Updates existing compilation's title, event list or pinned status
     *
     * @param compId  compilation id
     * @param requestDto new comp dto, title must not be blank
     * @return updates compilation with list of events
     * @throws ObjectNotFoundException if comp does not exist
     */
    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationOutputDto adminUpdateCompilation(@PathVariable Long compId,
                                                       @RequestBody @Valid CompilationUpdateDto requestDto) {
        log.info("Подборка обновлена");
        return compilationService.adminUpdateCompilation(compId, requestDto);
    }

}
