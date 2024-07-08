package ru.practicum.service;

import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void create(StatsDtoIn inputDTO);

    List<StatsDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
