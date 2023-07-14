package ru.practicum.utils.mapper;

import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static UserDto toUserDto(User user, Long rating) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .rating(rating)
                .build();
    }

    public static UserPublicDto toPublicUser(User user, Long rating) {
        return UserPublicDto.builder()
                .id(user.getId())
                .name(user.getName())
                .rating(rating)
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users, Map<User, Long> rating) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User u : users) {
            usersDto.add(toUserDto(u, rating.getOrDefault(u, 0L)));
        }
        return usersDto;
    }
}
