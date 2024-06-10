package ru.practicum.main.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryDtoResponse;
import ru.practicum.main.service.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@Slf4j
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDtoResponse>> findAll(
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("CategoryControllerPublic.findAll (from = {}, size = {})", from, size);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDtoResponse> findById(@PathVariable long catId) {
        log.info("CategoryControllerPublic.findById (id = {})", catId);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findById(catId));
    }

}
