package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.utils.enums.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class EventQueryCriteria {

    private List<Long> users;
    private List<EventState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String text;
    private Boolean onlyAvailable;
    private Boolean paid;
    private String sort;
}
