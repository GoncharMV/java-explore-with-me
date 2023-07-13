package ru.practicum.api.public_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.utils.exception.ObjectNotFoundException;

import java.util.List;

/**
 * public API for work with categories
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoriesPublicController {

    private final CategoriesService categoriesService;

    /**
     *
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of categories
     */
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Категории найдены");
        return categoriesService.getCategories(from, size);
    }

    /**
     *
     * @param catId category id
     * @return name of the category
     * @throws ObjectNotFoundException if category does not exist
     */
    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Категория найдена");
        return categoriesService.getCategory(catId);
    }

}
