package ru.practicum.user.service;

import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;

import java.util.List;

public interface UserService {

    UserDtoOut create(UserDtoIn inputDTO);

    List<UserDtoOut> getAll(List<Long> ids, int from, int size);

    void delete(long userId);
}
