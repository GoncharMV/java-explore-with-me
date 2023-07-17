package ru.practicum.utils.mapper;

import ru.practicum.compilations.dto.CompilationInputDto;
import ru.practicum.compilations.dto.CompilationOutputDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.events.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CompilationMapper {

    private CompilationMapper() {
    }

    public static Compilation toCompilation(CompilationInputDto dto) {
        return Compilation.builder()
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .build();
    }

    public static CompilationOutputDto toCompilationDto(Compilation compilation,
                                                        List<EventShortDto> events) {
        return CompilationOutputDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(events)
                .build();
    }

    public static List<CompilationOutputDto> toCompDtoList(List<Compilation> comps,
                                                           Map<Compilation, List<EventShortDto>> events) {
        List<CompilationOutputDto> compDto = new ArrayList<>();
        for (Compilation c : comps) {
            compDto.add(toCompilationDto(c, events.getOrDefault(c, List.of())));
        }
        return compDto;
    }

}
