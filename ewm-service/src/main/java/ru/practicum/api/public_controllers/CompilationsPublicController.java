package ru.practicum.api.public_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.service.CompilationService;
import ru.practicum.utils.exception.ObjectNotFoundException;

import java.util.List;

/**
 * public API for work with compilations
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsPublicController {

    private final CompilationService compilationService;

    /**
     *
     * @param pinned is on top of the home page
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of compilations with lists of events in each
     */
    @GetMapping
    public List<CompilationOutputDto> getCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Найдены подборки событий");
        return compilationService.getCompilations(pinned, from, size);
    }

    /**
     *
     * @param compId compilation id
     * @return specific compilation with the list of its events
     * @throws ObjectNotFoundException if compilation does not exist
     */
    @GetMapping("{compId}")
    public CompilationOutputDto getCompilation(@PathVariable Long compId) {
        log.info("Подборка событий найдена");
        return compilationService.getCompilation(compId);
    }

}
