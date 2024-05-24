package ru.practicum.service.service;

import ru.practicum.StatsInputDTO;
import ru.practicum.StatsOutputDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void create(StatsInputDTO inputDTO);

    List<StatsOutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
