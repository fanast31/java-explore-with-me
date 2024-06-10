package ru.practicum.main.service.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.category.dto.CategoryDtoRequest;
import ru.practicum.main.service.category.dto.CategoryDtoResponse;
import ru.practicum.main.service.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDtoResponse> post(@Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("CategoryControllerAdmin.post: {}", categoryDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryDtoRequest));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDtoResponse> pach(@PathVariable long catId,
                            @Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("CategoryControllerAdmin.pach: {}", categoryDtoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(catId, categoryDtoRequest));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable long catId) {
        log.info("CategoryControllerAdmin.delete: catId = {}", catId);
        categoryService.delete(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
