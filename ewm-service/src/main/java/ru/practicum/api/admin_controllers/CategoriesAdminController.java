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
        return service.adminAddCategory(requestDto);
    }

    @DeleteMapping("/{catId}")
    public void adminRemoveCategory(@PathVariable Long catId) {
        service.adminRemoveCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto adminUpdateCategory(@PathVariable Long catId,
                                    @RequestBody CategoryDto requestDto) {
        return service.adminUpdateCategory(catId, requestDto);
    }

}
