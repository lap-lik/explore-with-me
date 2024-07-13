package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dao.CategoryDAO;
import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dao.EventDAO;
import ru.practicum.exception.DataConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.EwmPageRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO categoryDAO;
    private final EventDAO eventDAO;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryDtoOut create(CategoryDtoIn inputDTO) {

        return mapper.entityToOutputDTO(categoryDAO.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    @Transactional
    public CategoryDtoOut update(long catId, CategoryDtoIn inputDTO) {

        checkExistsCategoryById(catId);

        Category savedCategory = categoryDAO.findById(catId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The category with the ID=`%d` was not found.", catId))
                        .build());
        savedCategory.setName(inputDTO.getName());

        return mapper.entityToOutputDTO(savedCategory);
    }

    @Override
    public CategoryDtoOut get(long catId) {

        checkExistsCategoryById(catId);
        return mapper.entityToOutputDTO(categoryDAO.findById(catId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The category with the ID=`%d` was not found.", catId))
                        .build()));
    }

    @Override
    public List<CategoryDtoOut> getAll(int from, int size) {

        Pageable pageable = EwmPageRequest.of(from, size);

        return mapper.entitiesToOutputDTOs(categoryDAO.findAll(pageable).getContent());
    }

    @Override
    @Transactional
    public void delete(long catId) {

        checkExistsCategoryById(catId);
        checkExistsEventByCategoryId(catId);

        categoryDAO.deleteById(catId);
    }

    private void checkExistsCategoryById(long catId) {

        boolean isExist = categoryDAO.existsById(catId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The category with the ID=`%d` was not found.", catId))
                    .build();
        }
    }

    private void checkExistsEventByCategoryId(long catId) {

        boolean isExist = eventDAO.existsById(catId);
        if (isExist) {
            throw DataConflictException.builder()
                    .message(String.format("The category with the ID=`%d` is not empty.", catId))
                    .build();
        }
    }
}
