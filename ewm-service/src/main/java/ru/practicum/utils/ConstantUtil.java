package ru.practicum.utils;

import java.time.format.DateTimeFormatter;

public final class ConstantUtil {

    public static final String DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATA_FORMATTER = DateTimeFormatter.ofPattern(DATA_FORMAT);

    public static final String USER = " user ";
    public static final String EVENT = " event ";
    public static final String CAT = " category ";
    public static final String REQUEST = " participation request ";
    public static final String COMP = " compilation ";
    public static final String DATA = " current data ";
    public static final String STATUS = " status ";
    public static final String RATING = " rating changes ";

    public static final String NOT_FOUND = " does not exist ";
    public static final String IS_EXISTS = " is already exists";
    public static final String NOT_AVAILABLE = " is not available";
    public static final String NO_ACCESS = " does not have access to ";
    public static final String EMAIL_EXISTS = "user with current email is already exists";
    public static final String ONLY_CREATOR = "only creator has access to this function";
    public static final String ONLY_ADMIN = "only administration has access to this function";
    public static final String IS_FINAL = " cannot be changed ";
    public static final String REQ_LIMIT = " limit is reached ";
    public static final String CREATOR_REQ = "event creator cannot apply to participate";
    public static final String CREATOR_RATING = "event creator cannot rate their events";

    public static final Integer LIKE = 1;
    public static final Integer DISLIKE = -1;

    public static final String VIEWS = "views";
    public static final String EVENT_DATE = "eventDate";
    public static final String RATING_STR = "rating";

    private ConstantUtil() {
    }
}
