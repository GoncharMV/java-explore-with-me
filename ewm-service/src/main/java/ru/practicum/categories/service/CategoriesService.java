package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

import java.util.List;

public interface CategoriesService {
    CategoryDto adminAddCategory(CategoryDto requestDto);

    void adminRemoveCategory(Long catId);

    CategoryDto adminUpdateCategory(Long catId,
                                    CategoryDto requestDto);

    List<CategoryDto> getCategories(int from,
                                    int size);

    CategoryDto getCategory(Long catId);
}
