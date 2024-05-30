package ru.practicum.ewmmain.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.ewmmain.request.dto.ParticipationDtoIn;
import ru.practicum.ewmmain.request.dto.ParticipationDtoOut;
import ru.practicum.ewmmain.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    ParticipationDtoOut entityToDto(Request entity);

    @Mapping(target = "id", ignore = true)
    Request dtoToEntity(ParticipationDtoIn inputDto);

    List<ParticipationDtoOut> entitiesToDtos(List<Request> entity);
}
