package ru.practicum.utils.mapper;

import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toCat(CategoryDto catDto) {
        return Category.builder()
                .id(catDto.getId())
                .name(catDto.getName())
                .build();
    }

    public static CategoryDto toCatDto(Category cat) {
        return CategoryDto.builder()
                .id(cat.getId())
                .name(cat.getName())
                .build();
    }

    public static List<CategoryDto> toCatsDto(List<Category> catList) {
        List<CategoryDto> catsDto = new ArrayList<>();
        for (Category c : catList) {
            catsDto.add(toCatDto(c));
        }
        return catsDto;
    }

}
