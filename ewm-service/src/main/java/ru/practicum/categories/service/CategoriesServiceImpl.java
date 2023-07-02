package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.model.Event;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.exception.RequestNotProcessedException;
import ru.practicum.utils.mapper.CategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository catRepository;
    private final FindEntityUtilService findEntity;

    @Override
    @Transactional
    public CategoryDto adminAddCategory(CategoryDto requestDto) {

        Category checkCat = catRepository.findCategoryByName(requestDto.getName());

        if (checkCat != null) {
            throw new RequestNotProcessedException("Категория с данным названием существует");
        }

        Category cat = catRepository.save(CategoryMapper.toCat(requestDto));
        return CategoryMapper.toCatDto(cat);
    }

    @Override
    @Transactional
    public void adminRemoveCategory(Long catId) {
        Category cat = findEntity.findCategoryOrElseThrow(catId);
        List<Event> events = findEntity.findCategoryEvents(cat);

        if (!events.isEmpty()) throw new RequestNotProcessedException("Невозможно удалить категорию");

        catRepository.delete(cat);
    }

    @Override
    @Transactional
    public CategoryDto adminUpdateCategory(Long catId, CategoryDto requestDto) {
        Category checkCat = catRepository.findCategoryByName(requestDto.getName());

        if (checkCat != null && !checkCat.getId().equals(catId)) {
            throw new RequestNotProcessedException("Категория с данным названием существует");
        }

        Category cat = findEntity.findCategoryOrElseThrow(catId);
        cat.setName(requestDto.getName());
        Category newCat = catRepository.save(cat);
        return CategoryMapper.toCatDto(newCat);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);

        return CategoryMapper.toCatsDto(catRepository.findAll(pageable).toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category cat = findEntity.findCategoryOrElseThrow(catId);
        return CategoryMapper.toCatDto(cat);
    }
}
