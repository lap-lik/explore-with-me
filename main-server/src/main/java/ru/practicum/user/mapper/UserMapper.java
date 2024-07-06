package ru.practicum.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User inputDTOToEntity(UserDtoIn inputDTO);

    UserDtoOut entityToOutputDTO(User user);

    List<UserDtoOut> entitiesToOutputDTOs(List<User> users);
}
