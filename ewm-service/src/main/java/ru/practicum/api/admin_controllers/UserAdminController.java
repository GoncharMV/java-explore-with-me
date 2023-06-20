package ru.practicum.api.admin_controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<UserDto> adminGetUsers(@RequestParam(name = "ids", required = false) List<Integer> ids,
                                       @RequestParam(name = "from", defaultValue = "0") int from,
                                       @RequestParam(name = "size", defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto adminAddUser(@RequestBody @Valid UserDto requestDto) {
        return userService.addUser(requestDto);
    }

    @DeleteMapping("/{userId}")
    public void adminRemoveUser(@PathVariable Long userId) {
        userService.removeUser(userId);
    }

}
