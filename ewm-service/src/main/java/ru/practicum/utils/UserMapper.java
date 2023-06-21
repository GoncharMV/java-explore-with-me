package ru.practicum.utils;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserPublicDto toPublicUser(User user) {
        return UserPublicDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(toUserDto(u));
        }
        return usersDto;
    }
}
