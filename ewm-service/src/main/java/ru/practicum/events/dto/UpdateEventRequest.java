package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.enums.StateAction;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UpdateEventRequest {

    private String annotation;
    private Long category;
    private String description;

    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}
