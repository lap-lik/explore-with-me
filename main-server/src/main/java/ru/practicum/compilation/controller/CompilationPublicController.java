package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationOutputDTO;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {

    private final CompilationService service;

    @GetMapping
    public List<CompilationOutputDTO> getAllCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                         @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                         @RequestParam(defaultValue = "10") @Positive int size) {

        log.info("START endpoint `method:GET /compilations` (compilation search by parameters).");

        return service.getAllByPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationOutputDTO getCompilation(@PathVariable Long compId) {

        log.info("START endpoint `method:GET /compilations/{compId}` (get compilation by id), compilation id: {}.", compId);

        return service.getByPublic(compId);
    }
}
