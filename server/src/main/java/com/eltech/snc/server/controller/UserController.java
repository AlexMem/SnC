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
    public UserEntity createUser(@RequestBody UserEntity name) {
        name.setId(userService.createUser(name));
        return name;
    }

    @PostMapping(value = "/findUser", consumes = "application/json", produces = "application/json")
    public Integer findUser(@RequestBody UserEntity name) {
        return userService.findUser(name.getName());
    }

    @GetMapping(value = "/findUser")
    public Integer findUser(@RequestParam String name){
        return userService.findUser(name);
    }
}
