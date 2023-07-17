package ru.practicum.dto;

import java.time.format.DateTimeFormatter;

public class StatsConstantUtils {
    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern(DATA_FORMAT);

    private StatsConstantUtils() {
    }

}