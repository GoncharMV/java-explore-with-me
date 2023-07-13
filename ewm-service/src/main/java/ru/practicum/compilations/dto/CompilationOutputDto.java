package ru.practicum.compilations.dto;

import lombok.*;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationOutputDto {

    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;

}
