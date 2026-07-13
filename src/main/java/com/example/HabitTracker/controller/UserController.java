package com.example.HabitTracker.controller;

import com.example.HabitTracker.model.Habit;
import com.example.HabitTracker.model.User;
import com.example.HabitTracker.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/{username}")
    public User getUserById(@PathVariable String username){
        return userService.findByUsername(username).orElseThrow(
                () -> new RuntimeException("Username does not exist")
        );
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }


}
