package ru.practicum.users.service;

import ru.practicum.users.dto.UserDto;

import java.util.List;

public interface UserService {


    void adminRemoveUser(Long userId);

    UserDto adminAddUser(UserDto requestDto);

    List<UserDto> adminGetUsers(List<Long> ids,
                                int from,
                                int size);
}
