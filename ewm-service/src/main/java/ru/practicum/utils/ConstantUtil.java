package ru.practicum.utils;

import java.time.format.DateTimeFormatter;

public final class ConstantUtil {

    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern(DATA_FORMAT);

}
