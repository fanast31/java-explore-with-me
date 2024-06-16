package ru.practicum.main.service.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.service.dto.category.CategoryDtoRequest;
import ru.practicum.main.service.dto.category.CategoryDtoResponse;
import ru.practicum.main.service.model.Category;

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
