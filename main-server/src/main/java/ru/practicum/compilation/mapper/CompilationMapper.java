package ru.practicum.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.compilation.dto.CompilationInputDTO;
import ru.practicum.compilation.dto.CompilationOutputDTO;
import ru.practicum.compilation.dto.CompilationUpdateDto;
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
    Compilation inputDtoToEntity(CompilationInputDTO inputDTO);

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

    CompilationOutputDTO entityToOutputDto(Compilation compilation);

    List<CompilationOutputDTO> entitiesToOutputDtos(List<Compilation> compilation);

    @Mapping(target = "events", source = "events", qualifiedByName = "mapToEventSet")
    Compilation update(CompilationUpdateDto dto, @MappingTarget Compilation compilation);
}
