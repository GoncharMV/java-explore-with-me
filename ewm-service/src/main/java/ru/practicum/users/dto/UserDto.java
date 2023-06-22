package ru.practicum.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @Email
    private String email;
}
