package ru.practicum.utils.mapper;

import ru.practicum.location.model.Location;
import ru.practicum.location.dto.LocationDto;

public final class LocationMapper {

    private LocationMapper() {
    }

    public static Location toLocation(LocationDto dto) {
        return Location.builder()
                .lon(dto.getLon())
                .lat(dto.getLat())
                .build();
    }

    public static LocationDto toLocationDto(Location loc) {
        return LocationDto.builder()
                .lat(loc.getLat())
                .lon(loc.getLon())
                .build();
    }

}
