package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsOutputDto {

    private String app;
    private String uri;
    private Long hits;

}
