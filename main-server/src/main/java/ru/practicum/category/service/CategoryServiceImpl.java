package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dao.CategoryDAO;
import ru.practicum.category.dto.CategoryDtoIn;
import ru.practicum.category.dto.CategoryDtoOut;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO dao;
    private final CategoryMapper mapper;

    @Override
    @Transactional
    public CategoryDtoOut create(CategoryDtoIn inputDTO) {

        return mapper.entityToOutputDTO(dao.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    @Transactional
    public CategoryDtoOut update(long catId, CategoryDtoIn inputDTO) {

        checkExistsCategoryById(catId);
        inputDTO.setId(catId);

        return mapper.entityToOutputDTO(dao.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public CategoryDtoOut get(long catId) {

        checkExistsCategoryById(catId);
        return mapper.entityToOutputDTO(dao.findById(catId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The category with the ID=`%d` was not found.", catId))
                        .build()));
    }

    @Override
    public List<CategoryDtoOut> getAll(int from, int size) {

        return mapper.entitiesToOutputDTOs(dao.findAll(from, size));
    }

    @Override
    @Transactional
    public void delete(long catId) {

        checkExistsCategoryById(catId);
        dao.deleteById(catId);
    }

    private void checkExistsCategoryById(long catId) {

        boolean isExist = dao.existsById(catId);
        if (!isExist) {
            throw NotFoundException.builder()
                    .message(String.format("The category with the ID=`%d` was not found.", catId))
                    .build();
        }
    }
}
