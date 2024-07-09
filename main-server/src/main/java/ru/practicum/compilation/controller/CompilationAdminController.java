package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.dto.CompilationDtoUpdate;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDtoOut createCompilation(@Valid @RequestBody final CompilationDtoIn inputDTO) {

        log.info("START endpoint `method:POST /admin/compilations` (create compilation), compilation title: {}.", inputDTO.getTitle());

        return service.createByAdmin(inputDTO);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId) {

        log.info("START endpoint `method:DELETE /admin/compilations/{compId}` (delete compilation), compilation id: {}.", compId);

        service.deleteByAdmin(compId);
    }


    @PatchMapping("/{compId}")
    public CompilationDtoOut updateCompilation(@PathVariable long compId,
                                               @Valid @RequestBody final CompilationDtoUpdate inputDTO) {

        log.info("START endpoint `method:PATCH /admin/compilations/{compId}` (update compilation), compilation id: {}.", compId);

        return service.updateByAdmin(compId, inputDTO);
    }
}
