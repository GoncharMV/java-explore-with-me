package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.repository.CategoriesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository repository;

    @Override
    public CategoryDto adminAddCategory(CategoryDto requestDto) {
        return null;
    }

    @Override
    public void adminRemoveCategory(Long catId) {

    }

    @Override
    public CategoryDto adminUpdateCategory(Long catId, CategoryDto requestDto) {
        return null;
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return null;
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        return null;
    }
}
