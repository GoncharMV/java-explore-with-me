package ru.practicum.rating.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventRatingDto {

    private Long likes;
    private Long dislikes;
}
