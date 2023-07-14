package ru.practicum.rating.service;

import ru.practicum.rating.dto.EventRatingDto;

public interface RatingService {

    void addLike(Long userId, Long eventId);

    void addDislike(Long userId, Long eventId);

    EventRatingDto getRating(Long eventId);

}
