package com.example.eventbot.bot;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.enums.NotificationLevel;
import com.example.eventbot.services.ChatService;
import com.example.eventbot.services.EventService;
import com.example.eventbot.services.TaskService;
import com.example.eventbot.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationSender {
    private final EventService eventService;
    private final TaskService taskService;

    @Scheduled(cron = "0 0 0/1 ? * *")
    public void check() throws ApplicationException {
        log.info("start checking notificatinos");
    }

    public void sendNotificationsByLevel(NotificationLevel level) throws ApplicationException {
        Timestamp now = Timestamp.from(Instant.now());
        List<Event> events = eventService.getByNotificationLevel(now, level);
        for (Event event : events) {
            List<Task> tasks = taskService.getAllTasksByEvent(event);
            for (Task task : tasks) {
                if (task.getDone()) continue;
                UserEntity responsible = task.getUserId();
                sendNotification(responsible.getTelegramId(), task);
            }
            event.setLastNotify(now);
            eventService.update(event);
        }
    }

    public void sendNotification(Long receiver, Task task) {
        log.info("send message to: ({})", receiver);
    }
}
