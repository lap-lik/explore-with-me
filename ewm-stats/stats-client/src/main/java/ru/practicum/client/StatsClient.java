package ru.practicum.client;

import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;

import java.util.List;

public interface StatsClient {

    void postStats(StatsDtoIn inputDTO);

    List<StatsDtoOut> getStats(String start, String end, List<String> uris, boolean unique);
}
