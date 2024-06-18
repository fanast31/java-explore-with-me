package ru.practicum.main.service.servise.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.dto.category.CategoryDtoRequest;
import ru.practicum.main.service.dto.category.CategoryDtoResponse;
import ru.practicum.main.service.exceptions.ConflictDataException;
import ru.practicum.main.service.exceptions.DataNotFoundException;
import ru.practicum.main.service.mapper.CategoryMapper;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.repository.CategoriesRepository;
import ru.practicum.main.service.repository.EventsRepository;
import ru.practicum.main.service.utils.PaginationUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    @Override
    public CategoryDtoResponse create(CategoryDtoRequest categoryDtoRequest) {
        if (categoriesRepository.existsByName(categoryDtoRequest.getName())) {
            throw new ConflictDataException("Name must be unique");
        }
        return CategoryMapper.toCategoryDtoResponse(
                categoriesRepository.save(CategoryMapper.toCategory(categoryDtoRequest)));
    }

    @Override
    public CategoryDtoResponse update(long catId, CategoryDtoRequest categoryDtoRequest) {

        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        if (category.getName().equals(categoryDtoRequest.getName())) {
            return CategoryMapper.toCategoryDtoResponse(category);
        }
        if (categoriesRepository.existsByName(categoryDtoRequest.getName())) {
            throw new ConflictDataException("Name must be unique");
        }

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

        if (eventsRepository.findAllByCategory(category).size() > 0) {
            throw new ConflictDataException("The category has events");
        }

        categoriesRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDtoResponse> findAll(int from, int size) {
        Pageable pageable = PaginationUtils.createPageable(from, size);
        return categoriesRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDtoResponse findById(long catId) {
        Category category = categoriesRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        return CategoryMapper.toCategoryDtoResponse(category);
    }

}
