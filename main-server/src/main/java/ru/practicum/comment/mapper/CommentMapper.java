package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.comment.dto.CommentDtoOut;
import ru.practicum.comment.model.Comment;
import ru.practicum.user.mapper.UserMapper;

@Mapper(componentModel = "spring",
        uses = UserMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "eventId", source = "event.id")
    CommentDtoOut entityToDto(Comment entity);
}
