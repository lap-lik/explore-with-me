package ru.practicum.ewmmain.user.service;

import ru.practicum.ewmmain.user.dto.UserDtoIn;
import ru.practicum.ewmmain.user.dto.UserDtoOut;

import java.util.List;

public interface UserService {

    UserDtoOut create(UserDtoIn inputDTO);

    List<UserDtoOut> getAll(List<Long> ids, int from, int size);

    void delete(long userId);
}
