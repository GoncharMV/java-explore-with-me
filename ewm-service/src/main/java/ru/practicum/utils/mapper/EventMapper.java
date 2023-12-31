package ru.practicum.utils.mapper;

import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.participation_request.model.Request;
import ru.practicum.rating.dto.EventRatingDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EventMapper {

    private EventMapper() {
    }

    public static Event toEvent(EventInputDto dto, User initiator, Category category, Location location) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle())
                .initiator(initiator)
                .category(category)
                .location(location)
                .build();
    }

    public static EventOutputFullDto toOutputDto(Event event, UserPublicDto user,
                                                 List<Request> confirmedRequests, Long views,
                                                 Map<Event, EventRatingDto> ratings) {
        return EventOutputFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCatDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(user)
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .rating(ratings.get(event))
                .build();
    }

    private static EventShortDto toShortDto(Event event, UserPublicDto user,
                                            List<Request> confirmedRequests,  Long views,
                                            Map<Event, EventRatingDto> ratings) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCatDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(user)
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .rating(ratings.get(event))
                .build();
    }

    public static List<EventShortDto> toEventShortList(List<Event> events,
                                                       Map<Event, UserPublicDto> users,
                                                       Map<Event, List<Request>> confirmedRequests,
                                                       Map<Long, Long> views,
                                                       Map<Event, EventRatingDto> ratings) {

        return events.stream().map(event ->
            toShortDto(event,
                    users.get(event),
                    confirmedRequests.getOrDefault(event, List.of()),
                    views.getOrDefault(event.getId(), 0L),
                    ratings))
                .collect(Collectors.toList());
    }

    public static List<EventOutputFullDto> toEventFullDtoList(List<Event> events,
                                                              Map<Event, UserPublicDto> users,
                                                              Map<Event, List<Request>> confirmedRequests,
                                                              Map<Long, Long> views,
                                                              Map<Event, EventRatingDto> ratings) {
        return events.stream().map(event ->
                        toOutputDto(event,
                                users.get(event),
                                confirmedRequests.getOrDefault(event, List.of()),
                                views.getOrDefault(event.getId(), 0L),
                                ratings))
                .collect(Collectors.toList());
    }
}
