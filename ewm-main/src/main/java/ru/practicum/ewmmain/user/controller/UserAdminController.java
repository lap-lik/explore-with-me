package ru.practicum.ewmmain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.user.dto.UserDtoIn;
import ru.practicum.ewmmain.user.dto.UserDtoOut;
import ru.practicum.ewmmain.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoOut createUser(@Valid @RequestBody final UserDtoIn inputDTO) {

        log.info("START endpoint `method:POST /admin/users` (create user), user name: {}.", inputDTO.getName());

        return service.create(inputDTO);
    }

    @GetMapping
    public List<UserDtoOut> getUsers(@RequestParam(required = false) List<Long> ids,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                     @RequestParam(defaultValue = "20") @Positive int size) {

        log.info("START endpoint `method:GET /admin/users` (get users by id list), id list size: {}.", ids.size());

        return service.getAll(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable long userId) {

        log.info("START endpoint `method:DELETE /admin/users/{userId}` (delete user), user id: {}.", userId);

        service.delete(userId);
    }
}
