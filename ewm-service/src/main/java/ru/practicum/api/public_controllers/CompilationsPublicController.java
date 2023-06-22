package ru.practicum.api.public_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationOutputDto> getCompilations(
            @RequestParam(name = "pinned", required = false) Boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Найдены подборки событий");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationOutputDto getCompilation(@PathVariable Long compId) {
        log.info("Подборка событий найдена");
        return compilationService.getCompilation(compId);
    }

}
