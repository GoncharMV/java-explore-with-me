package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.utils.ConstantUtil;
import ru.practicum.utils.enums.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventOutputFullDto {

    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime eventDate;
    private UserPublicDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;

}
