package ru.practicum.main.service.category.service;

import ru.practicum.main.service.category.dto.CategoryDtoRequest;
import ru.practicum.main.service.category.dto.CategoryDtoResponse;

import java.util.List;

public interface CategoryService {
    CategoryDtoResponse create(CategoryDtoRequest categoryDtoRequest);
    CategoryDtoResponse update(long catId, CategoryDtoRequest categoryDtoRequest);
    void delete(long catId);
    List<CategoryDtoResponse> findAll(int from, int size);
    CategoryDtoResponse findById(long catId);
}
