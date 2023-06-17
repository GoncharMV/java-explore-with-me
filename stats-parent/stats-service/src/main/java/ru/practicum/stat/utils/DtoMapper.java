package ru.practicum.stat.utils;

import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsOutputDto;
import ru.practicum.stat.model.Hit;

public class DtoMapper {

    private DtoMapper() {}

    public static Hit toHit(HitsInputDto inputDto) {
        return Hit.builder()
                .app(inputDto.getApp())
                .uri(inputDto.getUri())
                .ip(inputDto.getIp())
                .timestamp(inputDto.getTimestamp())
                .build();
    }

    public static StatsOutputDto toOutputDto(Hit input) {
        return StatsOutputDto.builder()
                .app(input.getApp())
                .uri(input.getUri())
                .build();
    }
}
