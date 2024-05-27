package ru.practicum.ewmmain.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.category.dto.CategoryOutputDTO;
import ru.practicum.ewmmain.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryOutputDTO> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                    @RequestParam(defaultValue = "20") @Positive int size) {

        log.info("START endpoint `method:GET /categories` (get all categories).");

        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryOutputDTO getCategoryById(@PathVariable long catId) {

        log.info("START endpoint `method:GET /categories/{catId}` (get category by id), category id: {}.", catId);

        return service.get(catId);
    }
}
