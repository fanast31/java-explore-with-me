package ru.practicum.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.StatsDtoRequest;
import ru.practicum.dto.StatsDtoResponse;
import ru.practicum.stats.server.model.EndpointStats;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static List<StatsDtoResponse> toListStatsDtoResponse(List<EndpointStats> requests, boolean isUnique) {
        List<StatsDtoResponse> responses;

        Map<String, List<EndpointStats>> requestsByUri = requests.stream()
                .collect(Collectors.groupingBy(EndpointStats::getUri));

        return null;
    }

}
