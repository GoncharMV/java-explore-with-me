package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.service.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationsAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationOutputDto adminAddCompilation(@RequestBody @Valid CompilationInputDto requestDto) {
        log.info("Подборка добавлена");
        return compilationService.adminAddCompilation(requestDto);
    }

    @DeleteMapping("{/compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminRemoveCompilation(@PathVariable Long compId) {
        log.info("Подборка удалена");
        compilationService.adminRemoveCompilation(compId);
    }

    @PatchMapping("{/compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationOutputDto adminUpdateCompilation(@PathVariable Long compId,
                                                        @RequestBody CompilationInputDto requestDto) {
        log.info("Подборка обновлена");
        return compilationService.adminUpdateCompilation(compId, requestDto);
    }

}
