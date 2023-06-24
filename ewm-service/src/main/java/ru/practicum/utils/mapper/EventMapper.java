package ru.practicum.utils.mapper;

import ru.practicum.categories.model.Category;
import ru.practicum.events.dto.EventInputDto;
import ru.practicum.events.dto.EventOutputDto;
import ru.practicum.events.model.Event;
import ru.practicum.location.model.Location;
import ru.practicum.users.model.User;

public final class EventMapper {

    private EventMapper() {
    }

    public static Event toEvent(EventInputDto dto, User initiator, Category category, Location location) {
        return Event.builder()
                .id(dto.getId())
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

    public static EventOutputDto toOutputDto(Event event) {
        return EventOutputDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCatDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toPublicUser(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();
    }

}
