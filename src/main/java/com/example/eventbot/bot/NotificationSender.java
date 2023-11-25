package com.example.eventbot.bot;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.services.ChatService;
import com.example.eventbot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationSender {
    private final UserService userService;
    private final ChatService chatService;

    @Scheduled(cron = "0 0 0/1 ? * * *")
    public void check() throws ApplicationException {
        log.info("start checking notificatinos");
    }

    public void sendNotification(Long receiver) {
        log.info("send message to: ({})", receiver);
    }
}
