package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@Slf4j
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> adminGetUsers(@RequestParam(name = "ids", required = false) List<Long> ids,
                                       @RequestParam(name = "from", defaultValue = "0") int from,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Пользователи найдены");
        return userService.adminGetUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto adminAddUser(@RequestBody @Valid UserDto requestDto) {
        log.info("Пользователь зарегистрирован");
        return userService.adminAddUser(requestDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminRemoveUser(@PathVariable Long userId) {
        log.info("Пользователь удален");
        userService.adminRemoveUser(userId);
    }

}
