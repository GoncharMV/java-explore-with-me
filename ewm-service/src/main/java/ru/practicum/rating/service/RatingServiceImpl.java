package ru.practicum.rating.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.rating.model.Rating;
import ru.practicum.rating.repository.RatingRepository;
import ru.practicum.utils.ConstantUtil;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Override
    @Transactional
    public void addLike(Long userId, Long eventId) {
        Rating rating = Rating.builder()
                            .userId(userId)
                            .eventId(eventId)
                            .rating(ConstantUtil.LIKE)
                            .build();

        ratingRepository.save(rating);
    }

    @Override
    @Transactional
    public void addDislike(Long userId, Long eventId) {
        Rating rating = Rating.builder()
                            .userId(userId)
                            .eventId(eventId)
                            .rating(ConstantUtil.DISLIKE)
                            .build();

        ratingRepository.save(rating);
    }

    @Override
    public EventRatingDto getRating(Long eventId) {
        List<Rating> likes = ratingRepository.findAllByEventIdAndRating(eventId, ConstantUtil.LIKE);
        List<Rating> dislikes = ratingRepository.findAllByEventIdAndRating(eventId, ConstantUtil.DISLIKE);
        return EventRatingDto.builder()
                .likes((long) likes.size())
                .dislikes((long) dislikes.size())
                .build();
    }

}
