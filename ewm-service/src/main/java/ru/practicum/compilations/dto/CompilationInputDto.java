package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationInputDto {

    private Long id;
    @Size(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;

}
