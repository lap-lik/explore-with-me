package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDtoIn;
import ru.practicum.compilation.dto.CompilationDtoOut;
import ru.practicum.compilation.dto.CompilationDtoUpdate;

import java.util.List;

public interface CompilationService {

    CompilationDtoOut createByAdmin(CompilationDtoIn inputDTO);

    void deleteByAdmin(long compId);

    CompilationDtoOut updateByAdmin(long compId, CompilationDtoUpdate updateDto);

    List<CompilationDtoOut> getAllByPublic(boolean pinned, int from, int size);

    CompilationDtoOut getByPublic(Long compId);
}
