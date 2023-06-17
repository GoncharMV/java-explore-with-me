package ru.practicum.stat.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitsInputDto;
import ru.practicum.dto.StatsOutputDto;
import ru.practicum.stat.service.StatsService;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public void saveHit(@RequestBody @Valid HitsInputDto requestBody) {
        log.info("Информация сохранена");
        statsService.saveHit(requestBody);
    }

    @GetMapping("/stats")
    public List<StatsOutputDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Статистика собрана");
        return statsService.getStats(start, end, uris, unique);
    }

}
