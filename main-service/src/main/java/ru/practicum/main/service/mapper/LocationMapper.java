package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.LocationDto;
import ru.practicum.main.service.model.event.Location;

@UtilityClass
public class LocationMapper {

    public static Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .build();
    }

}
