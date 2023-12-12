package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.services.ChatService;
import com.example.eventbot.services.EventService;
import com.example.eventbot.services.TaskService;
import com.example.eventbot.services.WishTaskService;
import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinishEventCommandStrategy implements CommandStrategy {
    private final TaskService taskService;
    private final EventService eventService;
    private final ChatService chatService;
    private final WishTaskService wishTaskService;
    
    @Override
    @Transactional
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command FINISH_TASK: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.INVALID_COMMAND.throwIfTrue(message.getChat().getType().equals("private"));
        ChatEntity chat = chatService.getChat(message.getChatId());
        Event event = eventService.getByChat(chat);
        List<Task> tasks = taskService.getAllTasksByEvent(event);
        for (Task task : tasks) {
            wishTaskService.deleteAllByTaskId(task);
        }
        for (Task task : tasks) {
            taskService.delete(task);
        }
        eventService.delete(event);
        String answer = "Событие успешно завершено";
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.FINISH_EVENT;
    }
}
