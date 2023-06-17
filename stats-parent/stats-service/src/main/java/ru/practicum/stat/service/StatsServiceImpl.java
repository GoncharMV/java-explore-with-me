package ru.practicum.stat.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.StatsRepository;
import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsOutputDto;
import ru.practicum.stat.utils.DtoMapper;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {


    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveHit(HitsInputDto requestBody) {
        statsRepository.save(DtoMapper.toHit(requestBody));
    }

    @Override
    public List<StatsOutputDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return null;
    }
}
