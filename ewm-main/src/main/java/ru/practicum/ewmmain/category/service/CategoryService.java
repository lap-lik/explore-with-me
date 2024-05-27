package ru.practicum.ewmmain.category.service;

import ru.practicum.ewmmain.category.dto.CategoryInputDTO;
import ru.practicum.ewmmain.category.dto.CategoryOutputDTO;

import java.util.List;

public interface CategoryService {

    CategoryOutputDTO create(CategoryInputDTO inputDTO);

    void delete(long catId);

    CategoryOutputDTO update(long catId, CategoryInputDTO inputDTO);

    CategoryOutputDTO get(long catId);

    List<CategoryOutputDTO> getAll(int from, int size);
}
