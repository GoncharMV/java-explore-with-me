package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPublicDto {

    private Long id;
    private String name;
    private Long rating;
}
