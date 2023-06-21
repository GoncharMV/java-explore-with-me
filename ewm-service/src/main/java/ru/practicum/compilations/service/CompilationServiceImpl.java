package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    @Override
    public CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto) {
        return null;
    }
}
