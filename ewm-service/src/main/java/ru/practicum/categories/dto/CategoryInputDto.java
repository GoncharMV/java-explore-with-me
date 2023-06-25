package ru.practicum.categories.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CategoryInputDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
