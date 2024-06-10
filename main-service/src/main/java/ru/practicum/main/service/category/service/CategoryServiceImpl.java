package ru.practicum.main.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.category.dto.CategoryDtoRequest;
import ru.practicum.main.service.category.dto.CategoryDtoResponse;
import ru.practicum.main.service.category.mapper.CategoryMapper;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoriesRepository;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.utils.PaginationUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoriesRepository categoriesRepository;

    @Override
    public CategoryDtoResponse create(CategoryDtoRequest categoryDtoRequest) {
        return CategoryMapper.toCategoryDtoResponse(
                categoriesRepository.save(CategoryMapper.toCategory(categoryDtoRequest)));
    }

    @Override
    public CategoryDtoResponse update(long catId, CategoryDtoRequest categoryDtoRequest) {

        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        if (Objects.equals(category.getName(), categoryDtoRequest.getName())) {
            return CategoryMapper.toCategoryDtoResponse(category);
        } else {
            category.setName(categoryDtoRequest.getName());
            return CategoryMapper.toCategoryDtoResponse(categoriesRepository.save(category));
        }

    }

    @Override
    public void delete(long catId) {
        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        categoriesRepository.delete(category);
    }

    @Override
    public List<CategoryDtoResponse> findAll(int from, int size) {
        Pageable pageable = PaginationUtils.createPageable(from, size);
        return categoriesRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDtoResponse findById(long catId) {
        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        return CategoryMapper.toCategoryDtoResponse(category);
    }

}
