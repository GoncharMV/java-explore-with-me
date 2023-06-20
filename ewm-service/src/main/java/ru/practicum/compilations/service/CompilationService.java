package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;

public interface CompilationService {

    CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto);

}
