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

import java.time.LocalDate;

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
        return eventRepository.save(event);
    }

    public Event getByChat(ChatEntity chat) throws ApplicationException {
        return eventRepository.findEventByChatId(chat).orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
    }
}
