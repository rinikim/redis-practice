package com.example.helloworld.service;

import com.example.helloworld.dto.UserProfile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final ExternalApiService externalApiService;
    private final StringRedisTemplate redisTemplate;

    public UserService(ExternalApiService externalApiService, StringRedisTemplate redisTemplate) {
        this.externalApiService = externalApiService;
        this.redisTemplate = redisTemplate;
    }

    public UserProfile getUserProfile(String userId) {
        String userName = null;

        // Cache-Aside 방법 적용
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String cacheName = ops.get("nameKey:" + userId);
        if (cacheName != null) {
            userName = cacheName;
        } else {
            // 외부에 요청하는 service
            userName = externalApiService.getUserName(userId);
            ops.set("nameKey:" + userId, userName, 5, TimeUnit.SECONDS);
        }


        int userAge = externalApiService.getUserAge(userId);

        return new UserProfile(userName, userAge);
    }
}
