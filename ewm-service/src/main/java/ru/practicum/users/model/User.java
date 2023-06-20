package ru.practicum.users.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
import lombok.*;


public class User {

    private Long id;
    private String name;
    private String email;
}
