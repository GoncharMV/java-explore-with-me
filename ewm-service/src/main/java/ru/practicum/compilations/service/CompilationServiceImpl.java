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
import ru.practicum.events.model.Event;
import ru.practicum.participation_request.model.Request;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.stats.StatsService;
import ru.practicum.utils.mapper.CompilationMapper;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final FindEntityUtilService findEntity;
    private final StatsService statsService;

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

        Map<Long, Long> views = statsService.getViews(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);

        return CompilationMapper.toCompilationDto(compilation, findEntity.findConfirmedRequestsMap(events),
                views, ratings);
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
        Map<Long, Long> views = statsService.getViews(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);
        return CompilationMapper.toCompilationDto(comp, findEntity.findConfirmedRequestsMap(events), views, ratings);
    }

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);
        List<Compilation> compilations;
        List<Event> events = new ArrayList<>();

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            compilations = compilationRepository.findAll(pageable).toList();
        }

        compilations.forEach(compilation -> events.addAll(compilation.getEvents()));
        Map<Event, List<Request>> confRequest = findEntity.findConfirmedRequestsMap(new ArrayList<>(events));
        Map<Long, Long> views = statsService.getViews(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);

        return CompilationMapper.toCompDtoList(compilations, confRequest, views, ratings);
    }

    @Override
    public CompilationOutputDto getCompilation(Long compId) {
        Compilation comp = findEntity.findCompilationOrThrow(compId);
        List<Event> events = comp.getEvents();
        Map<Long, Long> views = statsService.getViews(events);
        Map<Event, EventRatingDto> ratings = findEntity.findRatings(events);
        return CompilationMapper.toCompilationDto(comp, findEntity.findConfirmedRequestsMap(events), views, ratings);
    }
}
