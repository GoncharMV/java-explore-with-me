package ru.practicum.stat.utils;

import model.Hits;
import ru.practicum.dto.HitsInputDto;

public class DtoMapper {

    private DtoMapper() {}

    public static Hits toHit(HitsInputDto inputDto) {
        return Hits.builder()
                .app(inputDto.getApp())
                .uri(inputDto.getUri())
                .ip(inputDto.getIp())
                .timestamp(inputDto.getTimestamp())
                .build();
    }
}
