package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsInputDTO;
import ru.practicum.StatsOutputDTO;
import ru.practicum.service.dao.StatsDAO;
import ru.practicum.service.exception.ValidException;
import ru.practicum.service.mapper.StatsMapper;
import ru.practicum.service.model.StatsProjection;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsDAO statsDAO;
    private final StatsMapper mapper;

    @Override
    @Transactional
    public void create(StatsInputDTO inputDTO) {

        statsDAO.save(mapper.inputDTOToEntity(inputDTO));
    }

    @Override
    public List<StatsOutputDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (start.isAfter(end)) {
            throw ValidException.builder().message("Start date cannot be after end date").build();
        }

        if (unique) {
            if (uris == null) {
                return toStatsOutputDTOs(statsDAO.findAllStatsProjectionUniqueIp(start, end));
            } else {
                return toStatsOutputDTOs(statsDAO.findAllStatsProjectionUniqueIpAndUriIn(start, end, uris));
            }
        } else {
            if (uris == null) {
                return toStatsOutputDTOs(statsDAO.findAllStatsProjectionByTimestampBetween(start, end));
            } else {
                return toStatsOutputDTOs(statsDAO.findAllStatsProjectionByTimestampBetweenAndUriIn(start, end, uris));
            }
        }
    }

    private List<StatsOutputDTO> toStatsOutputDTOs(List<StatsProjection> stats) {
        Map<String, StatsOutputDTO> statsDTOMap = new HashMap<>();

        for (StatsProjection stat : stats) {
            String key = stat.getApp() + "_" + stat.getUri();
            if (!statsDTOMap.containsKey(key)) {
                statsDTOMap.put(key, StatsOutputDTO.builder()
                        .app(stat.getApp())
                        .uri(stat.getUri())
                        .hits(1L)
                        .build());
            } else {
                StatsOutputDTO dto = statsDTOMap.get(key);
                dto.setHits(dto.getHits() + 1);
            }
        }

        return statsDTOMap.values().stream()
                .sorted(Comparator.comparingLong(StatsOutputDTO::getHits).reversed())
                .collect(Collectors.toList());
    }
}
