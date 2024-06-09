package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.server.model.EndpointStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointStats, Long> {
    @Query("SELECT s FROM EndpointStats AS s " +
            "WHERE s.timestamp >= :start AND s.timestamp <= :end")
    List<EndpointStats> findAllStatsBetweenDates(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM EndpointStats AS s " +
            "WHERE s.timestamp >= :start AND s.timestamp <= :end " +
            "AND s.uri IN (:uris)")
    List<EndpointStats> findByTimestampAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
