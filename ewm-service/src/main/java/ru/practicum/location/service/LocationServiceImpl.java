package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;
import ru.practicum.utils.mapper.LocationMapper;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locRepository;

    @Override
    public Location getLocationOrElseSave(LocationDto location) {
        Location loc = LocationMapper.toLocation(location);

        return locRepository.find(loc.getLon(), loc.getLat())
                .orElseGet(() -> locRepository.save(loc));
    }
}
