package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.mapper.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FindEntityUtilService findEntity;

    @Override
    @Transactional
    public void adminRemoveUser(Long userId) {
        User user = findEntity.findUserOrElseThrow(userId);
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserDto adminAddUser(UserDto requestDto) {
        findEntity.checkUserEmailExists(requestDto.getEmail());

        User user = userRepository.save(UserMapper.toUser(requestDto));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> adminGetUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageableUtil.pageManager(from, size, null);
        List<User> users;

        if (ids == null) {
            users = userRepository.findAll(pageable).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageable);
        }

        return UserMapper.toUserDtoList(users);
    }
}
