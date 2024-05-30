package ru.practicum.service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsDtoIn;
import ru.practicum.StatsDtoOut;
import ru.practicum.service.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.service.model.StatsConstant.DATE_TIME_PATTERN;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    void createStats(@Valid @RequestBody final StatsDtoIn inputDTO) {

        log.info("START endpoint `method:POST /hit` (create stats), for uri: {}.", inputDTO.getUri());

        statsService.create(inputDTO);
    }

    @GetMapping("/stats")
    List<StatsDtoOut> getStats(@RequestParam() @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime start,
                               @RequestParam() @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime end,
                               @RequestParam(required = false) List<String> uris,
                               @RequestParam(defaultValue = "false") boolean unique) {

        log.info("START endpoint `method:GET /stats` (get all stats).");

        return statsService.getStats(start, end, uris, unique);
    }
}
