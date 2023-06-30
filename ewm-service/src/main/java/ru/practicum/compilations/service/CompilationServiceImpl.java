package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.model.Event;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.mapper.CompilationMapper;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final FindEntityUtilService findEntity;

    @Override
    @Transactional
    public CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto) {
        if (requestDto.getPinned() == null) requestDto.setPinned(false);

        List<Event> events = findEntity.findEventsByIds(requestDto.getEvents());

        Compilation compilation = CompilationMapper.toCompilation(requestDto);
        compilation.setEvents(events);

        compilation = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationDto(compilation, findEntity.findConfirmedRequestsMap(events));
    }

    @Override
    public void adminRemoveCompilation(Long compId) {
        Compilation compilation = findEntity.findCompilationOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public CompilationOutputDto adminUpdateCompilation(Long compId, CompilationInputDto requestDto) {
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
