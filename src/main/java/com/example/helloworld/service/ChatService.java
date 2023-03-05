package com.example.helloworld.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ChatService implements MessageListener {

    private final RedisMessageListenerContainer container;
    private final RedisTemplate<String, String> redisTemplate;

    public ChatService(RedisMessageListenerContainer container, RedisTemplate<String, String> redisTemplate) {
        this.container = container;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 사용자 입력을 받음
     * @param chatRoomName
     */
    public void enterCharRoom(String chatRoomName) {
        // 메세지를 입력받으면 해당 messageListener 에서 받을 수 있는데 이를 사용할 수 있게 구현
        container.addMessageListener(this, new ChannelTopic(chatRoomName));

        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()) {
            String line = in.nextLine();
            if (line.equals("q")) {
                System.out.println("Quit...");
                break;
            }

            // chatRoomNAme = topic
            redisTemplate.convertAndSend(chatRoomName, line);
        }

        container.removeMessageListener(this);
    }

    /**
     * redis sub 를 통해서 들어온 메세지를 확인가능
     * 비동기로 메세지가 도착할 때마다 확인해준다.
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("Message : " +  message.toString());
    }
}
