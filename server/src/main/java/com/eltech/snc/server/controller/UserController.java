package com.eltech.snc.server.controller;

import com.eltech.snc.server.jpa.entity.UserEntity;
import com.eltech.snc.server.services.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/createUser", consumes = "application/json", produces = "application/json")
    public Integer createUser(@RequestBody UserEntity name) {
        return userService.createUser(name);
    }
}
