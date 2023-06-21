package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.CategoriesService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoriesAdminController {

    private final CategoriesService service;

    @PostMapping
    public CategoryDto adminAddCategory(@RequestBody @Valid CategoryDto requestDto) {
        log.info("Категория добавлена");
        return service.adminAddCategory(requestDto);
    }

    @DeleteMapping("/{catId}")
    public void adminRemoveCategory(@PathVariable Long catId) {
        log.info("Категория удалена");
        service.adminRemoveCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto adminUpdateCategory(@PathVariable Long catId,
                                    @RequestBody CategoryDto requestDto) {
        log.info("Данные категории изменены");
        return service.adminUpdateCategory(catId, requestDto);
    }

}
