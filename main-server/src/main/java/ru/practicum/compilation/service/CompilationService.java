package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationInputDTO;
import ru.practicum.compilation.dto.CompilationOutputDTO;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationOutputDTO createByAdmin(CompilationInputDTO inputDTO);

    void deleteByAdmin(long compId);

    CompilationOutputDTO updateByAdmin(long compId, CompilationUpdateDto updateDto);

    List<CompilationOutputDTO> getAllByPublic(boolean pinned, int from, int size);

    CompilationOutputDTO getByPublic(Long compId);
}
