package com.example.helloworld.controller;

import com.example.helloworld.dto.UserProfile;
import com.example.helloworld.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final UserService userService;

    public ApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}/profile")
    public UserProfile getUserProfile(@PathVariable(value = "userId") String userId) {
        // 데이터는 자주 바뀌지 않되 요청량이 많다고 가정
        return userService.getUserProfile(userId);
    }
}
