package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.StatsInputDTO;
import ru.practicum.service.model.Stats;

import static ru.practicum.service.model.StatsConstant.DATE_TIME_PATTERN;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE_TIME_PATTERN)
    Stats inputDTOToEntity(StatsInputDTO inputDTO);
}
