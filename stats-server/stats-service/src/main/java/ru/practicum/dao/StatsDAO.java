package ru.practicum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Stats;
import ru.practicum.model.StatsProjection;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsDAO extends JpaRepository<Stats, Long> {

    @Query("SELECT s.app as app, s.uri as uri " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri")
    List<StatsProjection> findAllStatsProjectionUniqueIp(@Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);

    @Query("SELECT s.app as app, s.uri as uri " +
            "FROM Stats s " +
            "WHERE s.uri IN :uris " +
            "AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri")
    List<StatsProjection> findAllStatsProjectionUniqueIpAndUriIn(@Param("start") LocalDateTime start,
                                                                 @Param("end") LocalDateTime end,
                                                                 @Param("uris") List<String> uris);

    List<StatsProjection> findAllStatsProjectionByTimestampBetween(@Param("start") LocalDateTime start,
                                                                   @Param("end") LocalDateTime end);

    List<StatsProjection> findAllStatsProjectionByTimestampBetweenAndUriIn(@Param("start") LocalDateTime start,
                                                                           @Param("end") LocalDateTime end,
                                                                           @Param("uris") List<String> uris);
}
