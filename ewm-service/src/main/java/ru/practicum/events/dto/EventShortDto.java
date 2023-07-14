package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.utils.ConstantUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = ConstantUtil.DATA_FORMAT)
    private LocalDateTime eventDate;
    private Long id;
    private UserPublicDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
    private EventRatingDto rating;

}
