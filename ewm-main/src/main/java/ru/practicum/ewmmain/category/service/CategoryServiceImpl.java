package ru.practicum.ewmmain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmmain.category.dao.CategoryDAO;
import ru.practicum.ewmmain.category.dto.CategoryInputDTO;
import ru.practicum.ewmmain.category.dto.CategoryOutputDTO;
import ru.practicum.ewmmain.category.mapper.CategoryMapper;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDAO dao;
    private final CategoryMapper mapper;

    @Override
    public CategoryOutputDTO create(CategoryInputDTO inputDTO) {

        return mapper.entityToOutputDTO(dao.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public void delete(long catId) {

        checkExistsCategoryById(catId);

        dao.deleteById(catId);
    }

    @Override
    public CategoryOutputDTO update(long catId, CategoryInputDTO inputDTO) {

        checkExistsCategoryById(catId);
        inputDTO.setId(catId);

        return mapper.entityToOutputDTO(dao.save(mapper.inputDTOToEntity(inputDTO)));
    }

    @Override
    public CategoryOutputDTO get(long catId) {

        checkExistsCategoryById(catId);
        return mapper.entityToOutputDTO(dao.findById(catId)
                .orElseThrow(() -> NotFoundException.builder()
                        .message(String.format("The category with the ID=`%d` was not found.", catId))
                        .build()));
    }

    @Override
    public List<CategoryOutputDTO> getAll(int from, int size) {

        return mapper.entitiesToOutputDTOs(dao.findll(from, size));
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
