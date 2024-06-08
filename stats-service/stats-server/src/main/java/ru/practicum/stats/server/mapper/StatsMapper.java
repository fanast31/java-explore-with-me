package ru.practicum.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatsDtoRequest;
import ru.practicum.stats.server.model.EndpointStats;

@UtilityClass
public class StatsMapper {

    public static EndpointStats toEndpointStats(StatsDtoRequest hitDto) {
        return EndpointStats.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(hitDto.getTimestamp())
                .build();
    }

}
