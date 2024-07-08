package ru.practicum.event.mapper;

import org.mapstruct.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;

import java.util.List;

import static ru.practicum.constant.Constant.DATE_TIME_PATTERN;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "latitude", source = "lat"),
            @Mapping(target = "longitude", source = "lon")
    })
    Location locationDtoToLocation(LocationDto inputDto);

    @Mappings({
            @Mapping(target = "lat", source = "latitude"),
            @Mapping(target = "lon", source = "longitude")
    })
    LocationDto locationTolocationDto(Location entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "publishedOn", ignore = true),
            @Mapping(target = "category.id", source = "inputDto.category"),
            @Mapping(target = "eventDate", source = "inputDto.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "initiator.id", source = "userId"),
            @Mapping(target = "location", source = "location"),
            @Mapping(target = "state", source = "state")
    })
    Event eventDtoToEvent(EventDtoIn inputDto, Long userId, Location location, EventState state);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "publishedOn", ignore = true),
            @Mapping(target = "category.id", source = "inputDto.category"),
            @Mapping(target = "eventDate", source = "inputDto.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "initiator.id", source = "userId"),
            @Mapping(target = "state", source = "state")
    })
    Event eventDtoToEvent(EventDtoIn inputDto, Long userId, EventState state);

    @Mappings({
            @Mapping(target = "eventDate", source = "entity.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "views", source = "views", defaultValue = "0L"),
            @Mapping(target = "confirmedRequests", source = "confirmedRequests", defaultValue = "0L")
    })
    EventShortDtoOut eventToEventShortDto(Event entity, Long views, Long confirmedRequests);

    @Mappings({
            @Mapping(target = "createdOn", source = "entity.createdOn", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "eventDate", source = "entity.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "publishedOn", source = "entity.publishedOn", dateFormat = DATE_TIME_PATTERN)
    })
    EventDtoOut eventToEventDto(Event entity);

    @Mappings({
            @Mapping(target = "createdOn", source = "entity.createdOn", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "eventDate", source = "entity.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "publishedOn", source = "entity.publishedOn", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "views", source = "views", defaultValue = "0L"),
            @Mapping(target = "confirmedRequests", source = "confirmedRequests", defaultValue = "0L")
    })
    EventDtoOut eventToEventDto(Event entity, Long views, Long confirmedRequests);

    @Mappings({
            @Mapping(target = "eventDate", source = "entity.eventDate", dateFormat = DATE_TIME_PATTERN),
            @Mapping(target = "views", ignore = true),
            @Mapping(target = "confirmedRequests", ignore = true)
    })
    EventShortDtoOut eventToEventShortDto(Event entity);

    List<EventDtoOut> eventsToEventDtos(List<Event> entities);

    List<EventShortDtoOut> eventsToEventShortDtos(List<Event> entities);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = DATE_TIME_PATTERN)
    Event update(EventUserDtoUpdate updateDto, @MappingTarget Event entity);

    @Mapping(target = "eventDate", source = "eventDate", dateFormat = DATE_TIME_PATTERN)
    Event update(EventAdminDtoUpdate updateDto, @MappingTarget Event entity);
}
