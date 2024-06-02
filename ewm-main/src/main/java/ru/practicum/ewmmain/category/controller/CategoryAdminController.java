package ru.practicum.ewmmain.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.category.dto.CategoryDtoIn;
import ru.practicum.ewmmain.category.dto.CategoryDtoOut;
import ru.practicum.ewmmain.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {

    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDtoOut createCategory(@Valid @RequestBody final CategoryDtoIn inputDTO) {

        log.info("START endpoint `method:POST /admin/categories` (create category), category name: {}.", inputDTO.getName());

        return service.create(inputDTO);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive final long catId) {

        log.info("START endpoint `method:DELETE /admin/categories/{catId}` (delete category), category id: {}.", catId);

        service.delete(catId);
    }


    @PatchMapping("/{catId}")
    public CategoryDtoOut updateCategory(@PathVariable @Positive final long catId,
                                         @Valid @RequestBody final CategoryDtoIn inputDTO) {

        log.info("START endpoint `method:PATCH /admin/categories/{catId}` (update category), category id: {}.", catId);

        return service.update(catId, inputDTO);
    }
}
