package ru.practicum.stat.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsOutputDto;
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
        statsRepository.save(DtoMapper.toHit(requestBody));
    }

    @Override
    public List<StatsOutputDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris.isEmpty()) {
            if (unique) {
                // find all stats with dif ip
            } else {
                // find all stats with all ip
            }
        } else {
            if (unique) {
                // find stat by uri with dif ip
            } else {
                // find stat by uri with all ip

            }
        }
        return null;
    }
}
