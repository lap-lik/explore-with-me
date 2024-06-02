package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.StatsDtoIn;
import ru.practicum.model.Stats;

import static ru.practicum.model.StatsConstant.DATE_TIME_PATTERN;

@Mapper(componentModel = "spring")
public interface StatsMapper {
    @Mappings ({@Mapping(target = "id", ignore = true),
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE_TIME_PATTERN)})
    Stats inputDTOToEntity(StatsDtoIn inputDTO);
}
