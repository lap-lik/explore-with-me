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

    @Mappings({@Mapping(target = "event", source = "event.id"),
            @Mapping(target = "requester", source = "requester.id")})
    ParticipationDtoOut entityToDto(Request entity);


    @Mappings({@Mapping(target = "id", ignore = true),
            @Mapping(target = "event.id", source = "event"),
            @Mapping(target = "requester.id", source = "requester")})
    Request dtoToEntity(ParticipationDtoIn inputDto);

    List<ParticipationDtoOut> entitiesToDtos(List<Request> entity);
}
