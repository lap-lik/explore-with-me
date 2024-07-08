package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserDAO;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.util.EwmPageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserDAO dao;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDtoOut create(UserDtoIn inputDTO) {

        return mapper.entityToOutputDTO(dao.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public List<UserDtoOut> getAll(List<Long> ids, int from, int size) {

        Pageable pageable = EwmPageRequest.of(from, size, Sort.by("id"));
        List<User> users;
        if (Objects.isNull(ids) || ids.isEmpty()) {
            users = dao.findAll(pageable).getContent();
        } else {
            users = dao.findAllByIdIn(ids, pageable).getContent();
        }

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        return mapper.entitiesToOutputDTOs(users);
    }

    @Override
    @Transactional
    public void delete(long userId) {

        checkExistsUserById(userId);
        dao.deleteById(userId);
    }

    private void checkExistsUserById(long userId) {

        if (!dao.existsById(userId)) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID=`%d` was not found.", userId))
                    .build();
        }
    }
}
