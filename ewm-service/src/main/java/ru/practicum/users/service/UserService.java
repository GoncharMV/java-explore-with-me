package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {


    void removeUser(Long userId);

    UserDto addUser(UserDto requestDto);

    List<UserDto> getUsers(List<Integer> ids, int from, int size);
}
