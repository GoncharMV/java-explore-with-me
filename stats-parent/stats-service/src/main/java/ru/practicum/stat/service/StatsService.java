package ru.practicum.stat.service;

import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsOutputDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveHit(HitsInputDto requestBody);

    List<StatsOutputDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}
