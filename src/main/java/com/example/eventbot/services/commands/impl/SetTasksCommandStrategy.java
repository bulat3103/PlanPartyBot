package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.*;
import com.example.eventbot.services.*;
import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetTasksCommandStrategy implements CommandStrategy {
    private final UserService userService;
    private final ChatService chatService;
    private final EventService eventService;
    private final TaskService taskService;
    private final WishTaskService wishTaskService;

    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command SET_TASKS: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.INVALID_COMMAND.throwIfTrue(message.getChat().getType().equals("private"));
        ChatEntity chat = chatService.getChat(message.getChatId());
        Event event = eventService.getByChat(chat);
        if (event.getAdminId().getId() != userService.getByTag(message.getFrom().getUserName()).getId()) {
            return SendMessage
                    .builder()
                    .chatId(message.getChatId())
                    .text("Распределение заданий может осуществлять только создатель мероприятия!")
                    .build();
        }
        Set<ChatUser> chatUsers = chat.getChatUser();
        List<Task> allTasks = taskService.getAllTasksByEvent(event);
        Map<UserEntity, List<WishTask>> wish = new HashMap<>();
        Map<UserEntity, Integer> countTasksByUser = new HashMap<>();
        for (ChatUser chatUser : chatUsers) {
            wish.put(chatUser.getUser(), wishTaskService.getTasksByUser(chatUser.getUser(), event));
        }
        for (Task task : allTasks) {
            List<TaskToSort> sort = new ArrayList<>();
            for (ChatUser chatUser : chatUsers) {
                if (wish.get(chatUser.getUser()).size() == 0) {
                    int min = 1;
                    int max = allTasks.size() - min;
                    int random = (int) (Math.random() * (max + 1)) + min;
                    sort.add(new TaskToSort(
                            random,
                            countTasksByUser.getOrDefault(chatUser.getUser(), 0),
                            chatUser.getUser()
                    ));
                    continue;
                }
                WishTask wishTask = wish.get(chatUser.getUser()).stream().filter(t -> t.getId().getTaskId().equals(task)).findFirst().get();
                sort.add(new TaskToSort(
                        wishTask.getOrdinal(),
                        countTasksByUser.getOrDefault(chatUser.getUser(), 0),
                        chatUser.getUser()
                ));
            }
            Collections.sort(sort);
            TaskToSort who = sort.get(0);
            countTasksByUser.put(who.getUser(), countTasksByUser.getOrDefault(who.getUser(), 0) + 1);
            task.setUserId(who.getUser());
            taskService.update(task);
        }
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text("Распределение завершено!")
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.SET_TASKS;
    }
}

class TaskToSort implements Comparable<TaskToSort> {
    private Integer ord;
    private Integer count;
    private UserEntity user;

    public TaskToSort(Integer ord, Integer count, UserEntity user) {
        this.ord = ord;
        this.count = count;
        this.user = user;
    }

    @Override
    public int compareTo(TaskToSort o) {
        if (Objects.equals(this.count, o.count)) {
            return this.ord - o.ord;
        }
        return this.count - o.count;
    }

    public Integer getOrd() {
        return ord;
    }

    public Integer getCount() {
        return count;
    }

    public UserEntity getUser() {
        return user;
    }
}
