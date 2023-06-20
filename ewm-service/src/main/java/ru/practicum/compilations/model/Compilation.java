package ru.practicum.compilations.model;

import lombok.*;
import ru.practicum.events.model.Event;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compilation {

    private Long id;
    private String title;
    private Boolean pinned;
    private List<Event> events;
}
