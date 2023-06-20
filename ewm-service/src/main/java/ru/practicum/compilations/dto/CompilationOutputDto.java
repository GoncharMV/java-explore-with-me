package ru.practicum.compilations.dto;

import ru.practicum.events.dto.EventInputDto;

import java.util.List;

public class CompilationOutputDto {

    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventInputDto> events;

}
