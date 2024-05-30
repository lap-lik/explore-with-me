package ru.practicum.ewmmain.category.service;

import ru.practicum.ewmmain.category.dto.CategoryDtoIn;
import ru.practicum.ewmmain.category.dto.CategoryDtoOut;

import java.util.List;

public interface CategoryService {

    CategoryDtoOut create(CategoryDtoIn inputDTO);

    CategoryDtoOut update(long catId, CategoryDtoIn inputDTO);

    CategoryDtoOut get(long catId);

    List<CategoryDtoOut> getAll(int from, int size);

    void delete(long catId);
}
