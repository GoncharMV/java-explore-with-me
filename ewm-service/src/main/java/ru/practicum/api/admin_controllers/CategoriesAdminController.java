package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import javax.validation.Valid;

/**
 * admin API for work with event's categorisation
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoriesAdminController {

    private final CategoriesService service;

    /**
     * Creates new category
     *
     * @param requestDto new category data object, name must be unique
     * @return saved dto
     * @throws RequestNotProcessedException if name is not unique with Response Status 409
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto adminAddCategory(@RequestBody @Valid CategoryDto requestDto) {
        log.info("Категория добавлена");
        return service.adminAddCategory(requestDto);
    }

    /**
     * Updates name of the category
     *
     * @param catId unique id of category to change
     * @param requestDto new category data, name must be unique
     * @return saved dto
     * @throws ObjectNotFoundException if category does not exist with Response Status 404
     * @throws RequestNotProcessedException if name is not unique with Response Status 409
     */
    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto adminUpdateCategory(@PathVariable Long catId,
                                           @RequestBody @Valid CategoryDto requestDto) {
        log.info("Данные категории изменены");
        return service.adminUpdateCategory(catId, requestDto);
    }

    /**
     * Deletes existing category
     *
     * @param catId category id
     * @throws ObjectNotFoundException if category does not exist with Response Status 404
     * @throws RequestNotProcessedException if events under this category exist, with Response Status 409
     */
    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminRemoveCategory(@PathVariable Long catId) {
        log.info("Категория удалена");
        service.adminRemoveCategory(catId);
    }

}
