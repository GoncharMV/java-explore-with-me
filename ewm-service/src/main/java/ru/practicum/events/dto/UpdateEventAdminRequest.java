package ru.practicum.events.dto;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.utils.enums.EventState;
import ru.practicum.utils.enums.StateAction;

import java.time.LocalDateTime;

public class UpdateEventAdminRequest {


    private String annotation;
    private CategoryDto category;
    private String description;
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;

}
