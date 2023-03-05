package com.example.helloworld;

import com.example.helloworld.service.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class HelloworldApplication implements CommandLineRunner {

    private final ChatService chatService;

    public HelloworldApplication(ChatService chatService) {
        this.chatService = chatService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloworldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 채팅 실습을 위한 설정
        System.out.println("Application started..!");

        chatService.enterCharRoom("chat1");
    }
}
