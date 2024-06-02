package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {

//    private final CompilationService service;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public CompilationOutputDTO createCompilation(@Valid @RequestBody final CompilationInputDTO inputDTO) {
//
//        log.info("START endpoint `method:POST /admin/compilations` (create compilation), compilation name: {}.", inputDTO.getName());
//
//        return service.create(inputDTO);
//    }
//
//    @DeleteMapping("/{compId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteCompilation(@PathVariable long compId) {
//
//        log.info("START endpoint `method:DELETE /admin/compilations/{compId}` (delete compilation), compilation id: {}.", compId);
//
//        service.delete(compId);
//    }
//
//
//    @PatchMapping("/{compId}")
//    public CompilationOutputDTO updateCompilation(@PathVariable long compId,
//                                            @Valid @RequestBody final CompilationInputDTO inputDTO) {
//
//        log.info("START endpoint `method:PATCH /admin/compilations/{compId}` (update compilation), compilation id: {}.", compId);
//
//        return service.update(compId, inputDTO);
//    }
}
