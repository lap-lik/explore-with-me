package ru.practicum.ewmmain.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewmmain.category.dto.CategoryInputDTO;
import ru.practicum.ewmmain.category.dto.CategoryOutputDTO;
import ru.practicum.ewmmain.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category inputDTOToEntity(CategoryInputDTO inputDTO);

    CategoryOutputDTO entityToOutputDTO(Category entity);

    List<CategoryOutputDTO> entitiesToOutputDTOs(List<Category> entities);
}
