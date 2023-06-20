package ru.practicum.utils;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toUser(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserPublicDto toPublicUser(User user) {
        return UserPublicDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
