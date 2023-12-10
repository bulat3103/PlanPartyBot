package com.example.eventbot.bot;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.dto.NotificationDTO;
import com.example.eventbot.model.enums.NotificationLevel;
import com.example.eventbot.services.EventService;
import com.example.eventbot.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class NotificationSender {
    private final EventService eventService;
    private final TaskService taskService;
    private final MessageProducer producer;
    private final Session session;

    @Scheduled(cron = "0 0 0/1 ? * *")
    public void check() throws JMSException {
        log.info("start checking notificatinos");
        sendNotificationsByLevel(NotificationLevel.DAY);
        sendNotificationsByLevel(NotificationLevel.TWO_DAY);
        sendNotificationsByLevel(NotificationLevel.THREE_DAY);
        sendNotificationsByLevel(NotificationLevel.WEEK);
    }

    public void sendNotificationsByLevel(NotificationLevel level) throws JMSException {
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

    public void sendNotification(Long receiver, Task task) throws JMSException {
        log.info("send message to: ({})", receiver);
        NotificationDTO dto = new NotificationDTO(
                receiver,
                "Задача " + task.getName() + " еще не выполнена"
        );
        ObjectMessage message = session.createObjectMessage(dto);
        producer.send(message);
    }
}
