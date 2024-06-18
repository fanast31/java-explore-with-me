package ru.practicum.main.service.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.compilation.CompilationDto;
import ru.practicum.main.service.servise.compilation.CompilationService;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class ControllerPublicCompilation {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("ControllerPublicCompilation.getCompilations (pinned = {}, from = {}, size = {})",
                pinned, from, size);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable @Min(1) Long compId) {
        log.info("ControllerPublicCompilation.getCompilation compId: {}", compId);
        return ResponseEntity.status(HttpStatus.OK).body(compilationService.getCompilation(compId));
    }

}
