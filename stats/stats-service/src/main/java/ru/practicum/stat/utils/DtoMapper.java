package ru.practicum.stat.utils;

import ru.practicum.dto.HitsInputDto;
import ru.practicum.stat.model.Hit;

import java.time.LocalDateTime;

public final class DtoMapper {

    private DtoMapper() {

    }


    public static Hit toHit(HitsInputDto inputDto, LocalDateTime time) {
        return Hit.builder()
                .app(inputDto.getApp())
                .uri(inputDto.getUri())
                .ip(inputDto.getIp())
                .timestamp(time)
                .build();
    }

}