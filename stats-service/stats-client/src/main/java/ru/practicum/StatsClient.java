package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.StatsDtoRequest;
import ru.practicum.dto.StatsDtoResponse;
import ru.practicum.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class StatsClient {

    private final String statsServerUrl;
    private final RestTemplate restTemplate;

    public StatsClient(@Value("${stats.server.url}") String statsServerUrl) {
        this.statsServerUrl = statsServerUrl;
        this.restTemplate = new RestTemplate();
    }

    public void createHit(String app, HttpServletRequest request) {
        StatsDtoRequest statsDtoRequest = StatsDtoRequest.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        log.info("StatsClient.createHit app = {}, uri = {}, ip = {}",
                statsDtoRequest.getApp(),
                statsDtoRequest.getUri(),
                statsDtoRequest.getIp());

        restTemplate.postForObject(statsServerUrl + "/hit", statsDtoRequest, Object.class);
    }

    public List<StatsDtoResponse> getStatistics(
            LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        log.info("StatsClient.getStatistics start = {}, end = {}, uris = {}, unique = {}",
                start,
                end,
                uris == null ? "null" : String.join(", ", uris),
                unique);

        if (start == null || end == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Start and End parameters are required.");
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(statsServerUrl + "/stats");
        builder.queryParam("start", Utils.DATE_TIME_FORMATTER.format(start));
        builder.queryParam("end", Utils.DATE_TIME_FORMATTER.format(end));
        if (uris != null) builder.queryParam("uris", String.join(",", uris));
        if (unique != null) builder.queryParam("unique", unique);
        URI uri = builder.build(false).toUri();

        try {
            StatsDtoResponse[] stats = restTemplate.getForObject(uri, StatsDtoResponse[].class);

            if (stats != null) {
                return new ArrayList<>(Arrays.asList(stats));
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException.BadRequest ex) {
            throw ex;
        } catch (RestClientException ex) {
            log.error("Error while fetching statistics: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

}
