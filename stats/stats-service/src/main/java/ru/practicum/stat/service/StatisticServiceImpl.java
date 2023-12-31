package ru.practicum.stat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsConstantUtils;
import ru.practicum.dto.StatsOutputDto;
import ru.practicum.stat.exception.BadRequestException;
import ru.practicum.stat.repository.StatsRepository;
import ru.practicum.stat.utils.DtoMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class StatisticServiceImpl implements StatisticService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveHit(HitsInputDto requestBody) {
        statsRepository.save(DtoMapper.toHit(requestBody,
                LocalDateTime.parse(requestBody.getTimestamp(), StatsConstantUtils.DATA_FORMATTER)));
    }

    @Override
    public List<StatsOutputDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new BadRequestException("Невалидная дата");
        }

        if (uris == null || uris.isEmpty()) {
            if (Boolean.TRUE.equals(unique)) {
                return statsRepository.getAllStatsDistinctIp(start, end);
            } else {
                return statsRepository.getAllStats(start, end);
            }
        } else {
            if (Boolean.TRUE.equals(unique)) {
                return statsRepository.getStatsDistinctIp(start, end, uris);
            } else {
                return statsRepository.getStats(start, end, uris);
            }
        }
    }
}