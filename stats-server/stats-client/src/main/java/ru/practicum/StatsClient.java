package ru.practicum;

import java.util.List;

public interface StatsClient {

    void postStats(StatsDtoIn inputDTO);

    List<StatsDtoOut> getStats(String start, String end, List<String> uris, boolean unique);
}

