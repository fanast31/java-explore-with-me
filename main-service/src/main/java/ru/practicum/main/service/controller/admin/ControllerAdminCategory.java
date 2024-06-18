package ru.practicum.main.service.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.category.CategoryDtoRequest;
import ru.practicum.main.service.dto.category.CategoryDtoResponse;
import ru.practicum.main.service.servise.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class ControllerAdminCategory {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDtoResponse> create(@Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("CategoryControllerAdmin.create categoryDtoRequest: {}", categoryDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryDtoRequest));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDtoResponse> update(
            @PathVariable long catId,
            @Valid @RequestBody CategoryDtoRequest categoryDtoRequest) {
        log.info("CategoryControllerAdmin.update: catId = {}, categoryDtoRequest = {}", catId, categoryDtoRequest);
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(catId, categoryDtoRequest));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable long catId) {
        log.info("CategoryControllerAdmin.delete: catId = {}", catId);
        categoryService.delete(catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
