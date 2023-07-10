package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;
import ru.practicum.utils.exception.ObjectNotFoundException;
import ru.practicum.utils.exception.RequestNotProcessedException;

import javax.validation.Valid;
import java.util.List;

/**
 * admin API for work with users
 * all inputs must be json type
 */
@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    /**
     * Get list of full user dto with ids, names and emails
     *
     * @param ids list of user id to find
     * @param from specifies the index of the first displayed element from the list (default = 0)
     * @param size determines the number of elements to be displayed (default = 10)
     * @return list of users. If ids are null, return list of all users
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> adminGetUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                       @RequestParam(name = "from", defaultValue = "0") int from,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Пользователи найдены");
        return userService.adminGetUsers(ids, from, size);
    }

    /**
     * User registration
     *
     * @param requestDto user dto, email must be unique and valid
     * @return saved user
     * @throws RequestNotProcessedException if email is not unique
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto adminAddUser(@RequestBody @Valid UserDto requestDto) {
        log.info("Пользователь зарегистрирован");
        return userService.adminAddUser(requestDto);
    }

    /**
     * Delete user's registration
     *
     * @param userId user's id
     * @throws ObjectNotFoundException if user does not exist
     */
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminRemoveUser(@PathVariable Long userId) {
        log.info("Пользователь удален");
        userService.adminRemoveUser(userId);
    }

}
