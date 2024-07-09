package ru.practicum.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.dto.CompilationDtoUpdate;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = EventMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {

    @Mapping(target = "events", source = "events", qualifiedByName = "mapToEventSet")
    Compilation inputDtoToEntity(CompilationDtoIn inputDTO);

    @Named("mapToEventSet")
    default Set<Event> mapToEventSet(Set<Long> events) {
        if (events == null) {
            return Collections.emptySet();
        }
        return events.stream()
                .map(eventId -> {
                    Event event = new Event();
                    event.setId(eventId);
                    return event;
                })
                .collect(Collectors.toSet());
    }

    CompilationDtoOut entityToOutputDto(Compilation compilation);

    List<CompilationDtoOut> entitiesToOutputDtos(List<Compilation> compilation);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapToEventSet")
    Compilation update(CompilationDtoUpdate dto, @MappingTarget Compilation compilation);
}
