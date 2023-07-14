package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserPublicDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;
import ru.practicum.utils.FindEntityUtilService;
import ru.practicum.utils.PageableUtil;
import ru.practicum.utils.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return UserMapper.toUserDto(user, findUserRating(user));
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

        Map<User, Long> rating = new HashMap<>();
        users.forEach(user -> rating.put(user, findUserRating(user)));

        return UserMapper.toUserDtoList(users, rating);
    }

    @Override
    public Map<Event, UserPublicDto> findUsersEvents(List<Event> events) {
        Map<Event, UserPublicDto> eventUserMap = new HashMap<>();

        events.forEach(event -> {
            User user = event.getInitiator();
            eventUserMap.put(event, getPublicUser(user));
        });
        return eventUserMap;
    }

    private UserPublicDto getPublicUser(User user) {
        return UserMapper.toPublicUser(user, findUserRating(user));
    }

    private Long findUserRating(User user) {
        List<Event> events = findEntity.findUserEvents(user);
        Map<Event, Long> ratings = new HashMap<>();
        events.forEach(event -> ratings.put(event, findEntity.getAvgRating(event)));
        Long sum = 0L;
        for (Long r : ratings.values()) {
            sum += r;
        }

        if (ratings.size() != 0) {
            return sum / ratings.size();
        } else {
            return 0L;
        }
    }
}
