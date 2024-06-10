package ru.practicum.main.service.category.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.category.dto.CategoryDtoRequest;
import ru.practicum.main.service.category.dto.CategoryDtoResponse;
import ru.practicum.main.service.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public static Category toCategory(CategoryDtoRequest categoryDtoRequest) {
        return Category.builder()
                .name(categoryDtoRequest.getName())
                .build();
    }

    public static CategoryDtoResponse toCategoryDtoResponse(Category category) {
        return CategoryDtoResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
