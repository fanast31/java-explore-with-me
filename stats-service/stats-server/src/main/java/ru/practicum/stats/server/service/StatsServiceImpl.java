package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.StatsDtoRequest;
import ru.practicum.dto.StatsDtoResponse;
import ru.practicum.stats.server.mapper.StatsMapper;
import ru.practicum.stats.server.model.EndpointStats;
import ru.practicum.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void createHit(StatsDtoRequest statsDtoRequest) {
        statsRepository.save(StatsMapper.toEndpointStats(statsDtoRequest));
    }

    @Override
    public List<StatsDtoResponse> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean isUnique) {

        List<EndpointStats> endpointStats;

        if (uris == null || uris.isEmpty()) {
            endpointStats = statsRepository.findAllStatsBetweenDates(start, end);
        } else {
            endpointStats = statsRepository.findByTimestampAndUris(start, end, uris);
        }

        Map<String, List<EndpointStats>> requestsByUri = endpointStats.stream()
                .collect(Collectors.groupingBy(EndpointStats::getUri));

        return requestsByUri.entrySet().stream()
                .map(entry -> {
                    String uri = entry.getKey();
                    List<EndpointStats> hits = entry.getValue();
                    long count = isUnique
                            ? hits.stream().map(EndpointStats::getIp).distinct().count()
                            : hits.size();
                    return StatsDtoResponse.builder()
                            .app(hits.get(0).getApp())
                            .uri(uri)
                            .hits(count)
                            .build();
                })
                .sorted(Comparator.comparing(StatsDtoResponse::getHits).reversed())
                .collect(Collectors.toList());

    }

}
