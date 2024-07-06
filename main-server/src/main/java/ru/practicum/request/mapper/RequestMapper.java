package ru.practicum.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.practicum.request.dto.ParticipationDtoOut;
import ru.practicum.request.model.Request;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mappings({@Mapping(target = "event", source = "event.id"),
            @Mapping(target = "requester", source = "requester.id")})
    ParticipationDtoOut entityToDto(Request entity);

    List<ParticipationDtoOut> entitiesToDtos(List<Request> entity);
}
