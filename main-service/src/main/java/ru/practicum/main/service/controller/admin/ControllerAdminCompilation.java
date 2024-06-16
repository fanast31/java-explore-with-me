package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.compilation.CompilationDto;
import ru.practicum.main.service.dto.compilation.CompilationDtoRequest;
import ru.practicum.main.service.dto.compilation.UpdateCompilationRequest;
import ru.practicum.main.service.servise.compilation.CompilationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
@Slf4j
public class ControllerAdminCompilation {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(
            @RequestBody @Valid CompilationDtoRequest compilationDtoRequest) {
        log.info("ControllerAdminCompilation.createCompilation compilationDtoRequest: " + compilationDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                compilationService.createCompilation(compilationDtoRequest));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(
            @PathVariable Long compId,
            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("ControllerAdminCompilation.updateCompilation compId: {}, updateCompilationRequest: {}",
                compId, updateCompilationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                compilationService.updateCompilation(compId, updateCompilationRequest));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        log.info("ControllerAdminCompilation.deleteCompilation compId: {}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}