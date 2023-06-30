package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;

import java.util.List;

public interface CompilationService {

    CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto);

    List<CompilationOutputDto> getCompilations(Boolean pinned, int from, int size);

    CompilationOutputDto getCompilation(Long compId);

    void adminRemoveCompilation(Long compId);

    CompilationOutputDto adminUpdateCompilation(Long compId, CompilationInputDto requestDto);
}
