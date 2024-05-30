package ru.practicum.ewmmain.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmmain.user.dto.UserDtoIn;
import ru.practicum.ewmmain.user.dto.UserDtoOut;
import ru.practicum.ewmmain.user.dto.UserShortDtoOut;
import ru.practicum.ewmmain.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User inputDTOToEntity(UserDtoIn inputDTO);

    UserDtoOut entityToOutputDTO(User user);

    UserShortDtoOut entityToShortOutputDTO(User user);

    List<UserDtoOut> entitiesToOutputDTOs(List<User> users);
}
