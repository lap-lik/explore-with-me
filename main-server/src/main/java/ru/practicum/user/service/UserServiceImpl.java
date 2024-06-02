package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dao.UserDAO;
import ru.practicum.user.dto.UserDtoIn;
import ru.practicum.user.dto.UserDtoOut;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

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

        return mapper.entitiesToOutputDTOs(dao.findAll(ids, from, size));
    }

    @Override
    @Transactional
    public void delete(long userId) {

        checkExistsUserById(userId);
        dao.deleteById(userId);
    }

    private void checkExistsUserById(long userId) {

        boolean isExist = dao.existsById(userId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The user with the ID=`%d` was not found.", userId))
                    .build();
        }
    }
}
