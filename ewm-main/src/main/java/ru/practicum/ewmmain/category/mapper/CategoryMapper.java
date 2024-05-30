package ru.practicum.ewmmain.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmmain.category.dto.CategoryDtoIn;
import ru.practicum.ewmmain.category.dto.CategoryDtoOut;
import ru.practicum.ewmmain.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category inputDTOToEntity(CategoryDtoIn inputDTO);

    CategoryDtoOut entityToOutputDTO(Category entity);

    List<CategoryDtoOut> entitiesToOutputDTOs(List<Category> entities);
}
