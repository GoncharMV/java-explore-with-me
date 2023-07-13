package ru.practicum.location.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDto {

    private float lat;
    private float lon;

}
