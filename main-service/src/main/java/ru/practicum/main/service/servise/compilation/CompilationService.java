package ru.practicum.main.service.servise.compilation;

import ru.practicum.main.service.dto.compilation.CompilationDto;
import ru.practicum.main.service.dto.compilation.CompilationDtoRequest;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(CompilationDtoRequest compilationDtoRequest);

    CompilationDto updateCompilation(Long compId, CompilationDtoRequest compilationDtoRequest);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(long compId);

    void deleteCompilation(Long compId);

}