package com.example.eventbot.services;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.enums.NotificationLevel;
import com.example.eventbot.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Event create(String name, NotificationLevel level, LocalDate date, ChatEntity chat, UserEntity user) {
        Event event = new Event();
        event.setName(name);
        event.setChatId(chat);
        event.setLevel(level);
        event.setUntil(date);
        event.setAdminId(user);
        event.setLastNotify(Timestamp.from(Instant.now()));
        return eventRepository.save(event);
    }

    public Event getByChat(ChatEntity chat) throws ApplicationException {
        return eventRepository.findEventByChatId(chat).orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
    }

    public List<Event> getByNotificationLevel(Timestamp time, NotificationLevel level) {
        List<Event> all = eventRepository.findAll();
        return all.stream().filter(e -> e.getLevel().equals(level)).filter(e -> e.getLastNotify().before(time)).collect(Collectors.toList());
    }

    public void update(Event event) {
        eventRepository.save(event);
    }
}
