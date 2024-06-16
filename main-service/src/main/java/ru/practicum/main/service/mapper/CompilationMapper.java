package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.compilation.CompilationDto;
import ru.practicum.main.service.dto.compilation.CompilationDtoRequest;
import ru.practicum.main.service.model.Compilation;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public static Compilation toCompilation(CompilationDtoRequest compilationDtoRequest) {
        return Compilation.builder()
                .pinned(compilationDtoRequest.isPinned())
                .title(compilationDtoRequest.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .title(compilation.getTitle())
                .build();
    }
}
