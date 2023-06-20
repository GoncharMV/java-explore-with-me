package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public void removeUser(Long userId) {

    }

    @Override
    public UserDto addUser(UserDto requestDto) {
        return null;
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, int from, int size) {
        return null;
    }
}
