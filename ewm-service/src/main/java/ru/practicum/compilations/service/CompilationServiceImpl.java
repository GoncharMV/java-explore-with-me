package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    @Override
    @Transactional
    public CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto) {
        return null;
    }

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, int from, int size) {
        return null;
    }

    @Override
    public CompilationOutputDto getCompilation(Long compId) {
        return null;
    }
}
