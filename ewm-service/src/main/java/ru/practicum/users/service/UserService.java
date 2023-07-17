package ru.practicum.users.service;

import ru.practicum.events.model.Event;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserPublicDto;

import java.util.List;
import java.util.Map;

public interface UserService {


    void adminRemoveUser(Long userId);

    UserDto adminAddUser(UserDto requestDto);

    List<UserDto> adminGetUsers(List<Long> ids,
                                int from,
                                int size);

    Map<Event, UserPublicDto> findUsersEvents(List<Event> events);
}
