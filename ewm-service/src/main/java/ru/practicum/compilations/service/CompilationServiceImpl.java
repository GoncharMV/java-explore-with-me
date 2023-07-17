package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.dto.CompilationUpdateDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.events.service.EventService;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.mapper.CompilationMapper;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final FindEntityUtilService findEntity;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationOutputDto adminAddCompilation(CompilationInputDto requestDto) {
        if (requestDto.getPinned() == null) requestDto.setPinned(false);

        List<Event> events = new ArrayList<>();
        Compilation compilation = CompilationMapper.toCompilation(requestDto);

        if (requestDto.getEvents() != null) {
            events = findEntity.findEventsByIds(requestDto.getEvents());
        }

        compilation.setEvents(events);
        compilation = compilationRepository.save(compilation);

        return CompilationMapper.toCompilationDto(compilation, eventService.toShortDtoList(events));
    }

    @Override
    @Transactional
    public void adminRemoveCompilation(Long compId) {
        Compilation compilation = findEntity.findCompilationOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public CompilationOutputDto adminUpdateCompilation(Long compId, CompilationUpdateDto requestDto) {
        Compilation comp = findEntity.findCompilationOrThrow(compId);
        List<Event> events = new ArrayList<>();

        if (requestDto.getTitle() != null) {
            comp.setTitle(requestDto.getTitle());
        }

        if (requestDto.getPinned() != null) {
            comp.setPinned(requestDto.getPinned());
        }

        if (requestDto.getEvents() != null) {
            events = findEntity.findEventsByIds(requestDto.getEvents());
            comp.setEvents(events);
        }

        return CompilationMapper.toCompilationDto(comp, eventService.toShortDtoList(events));
    }

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;
        List<Event> events = new ArrayList<>();
        Pageable pageable = PageableUtil.pageManager(from, size, "events");

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        Map<Compilation, List<EventShortDto>> compEvents = new HashMap<>();

        compilations.forEach((Compilation compilation) -> {
            events.addAll(compilation.getEvents());
            compEvents.put(compilation, eventService.toShortDtoList(events));
        });

        return CompilationMapper.toCompDtoList(compilations, compEvents);
    }

    @Override
    public CompilationOutputDto getCompilation(Long compId) {
        Compilation comp = findEntity.findCompilationOrThrow(compId);
        List<Event> events = comp.getEvents();

        return CompilationMapper.toCompilationDto(comp, eventService.toShortDtoList(events));
    }
}
