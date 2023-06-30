package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationInputDto {

    @Size(min = 1, max = 50)
    @NotBlank
    @NotNull
    private String title;
    private Boolean pinned;
    private List<Long> events;

}
