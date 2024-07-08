package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationInputDTO;
import ru.practicum.compilation.dto.CompilationOutputDTO;
import ru.practicum.compilation.dto.CompilationUpdateDto;
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
    public CompilationOutputDTO createCompilation(@Valid @RequestBody final CompilationInputDTO inputDTO) {

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
    public CompilationOutputDTO updateCompilation(@PathVariable long compId,
                                                  @Valid @RequestBody final CompilationUpdateDto inputDTO) {

        log.info("START endpoint `method:PATCH /admin/compilations/{compId}` (update compilation), compilation id: {}.", compId);

        return service.updateByAdmin(compId, inputDTO);
    }
}
