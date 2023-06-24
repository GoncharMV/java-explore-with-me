package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.utils.enums.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOutputDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserPublicDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;

}
