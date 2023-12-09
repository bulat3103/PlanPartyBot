package com.example.eventbot.services;

import com.example.eventbot.bot.MessageExecutor;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.ChatEntity;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.Task;
import com.example.eventbot.model.UserEntity;
import com.example.eventbot.model.dto.CreateEventDto;
import com.example.eventbot.model.enums.NotificationLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionService {
    private final UserService userService;
    private final ChatService chatService;
    private final EventService eventService;
    private final TaskService taskService;
    private final WishTaskService wishTaskService;
    private final MessageExecutor messageExecutor;

    public void joinChatByCode(Message message) throws TelegramApiException {
        log.info("invoke joinChatByCode: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        ExceptionDescriptor.JOIN_ERROR.throwIfFalse(chatService.checkCode(Integer.valueOf(message.getText().trim())));
        userService.addUserToChat(message.getFrom().getId(), Integer.valueOf(message.getText().trim()));
        String answer = "Спасибо за присоединение!";
        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build());
    }

    public void createEventByChatCode(Message message, CreateEventDto dto) throws TelegramApiException {
        log.info("invoke createEventByChatCode: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        ExceptionDescriptor.JOIN_ERROR.throwIfFalse(chatService.checkCode(dto.getChatCode()));
        UserEntity user = userService.getByTag(message.getFrom().getUserName());
        ChatEntity chat = chatService.getChatByCode(dto.getChatCode());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dto.getDate(), formatter);
        Event event = eventService.create(dto.getName(), NotificationLevel.ONE_TIME_DAY, date, chat, user);
        String[] tasks = dto.getListOfWork().split("\n");
        for (int i = 0; i < tasks.length; i++) {
            taskService.create(event, tasks[i], i + 1);
        }
        String answer = "Событие создано!";
        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(chat.getTelegramId())
                .text("Пользователь @" + user.getUserTag() + " создал мероприятие " + event.getName())
                .build());
        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build());
    }

    public Event getAllTasksForEvent(Message message) throws TelegramApiException {
        log.info("invoke getAllTasksForEvent: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        ExceptionDescriptor.JOIN_ERROR.throwIfFalse(chatService.checkCode(Integer.valueOf(message.getText().trim())));
        ChatEntity chat = chatService.getChatByCode(Integer.valueOf(message.getText().trim()));
        Event event = eventService.getByChat(chat);
        List<Task> tasks = taskService.getAllTasksByEvent(event);
        String answer = "Список доступных дел:\n";
        for (Task task : tasks) {
            if (task.getDone()) continue;
            answer += task.getOrdinal() + ". " + task.getName() + "\n";
        }
        answer += "Введите список дел, которые вы бы хотели сделать через запятую (1,2,3).";
        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build());
        return event;
    }

    public void setWish(Message message, Event event) throws TelegramApiException {
        log.info("invoke getAllTasksForEvent: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        UserEntity user = userService.getByTag(message.getFrom().getUserName());
        String[] tasksIds = message.getText().trim().split(",");
        List<Task> tasks = taskService.getAllTasksByEvent(event);
        for (int i = 0; i < tasksIds.length; i++) {
            int ord = Integer.parseInt(tasksIds[i]);
            Task task = tasks.stream().filter(t -> t.getOrdinal() == ord).findFirst().orElseThrow(ExceptionDescriptor.APPLICATION_ERROR::exception);
            if (task.getDone()) continue;
            wishTaskService.create(task, user, i + 1);
        }
        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text("Ваши предпочтения сохранены! Спасибо!")
                .build());
    }

    public void setNotificationLevel(Update update, String callBackData) throws TelegramApiException {
        log.info("invoke setNotificationLevel: ({}, {})", update.getCallbackQuery().getFrom().getId(), callBackData);
        Long userTelegramId = update.getCallbackQuery().getFrom().getId();
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(userTelegramId));
        NotificationLevel newLevel = NotificationLevel.valueOf(callBackData);
        String answer = "Настройки уведомлений изменены";
        Long chatTelegramId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText message = new EditMessageText();
        message.setChatId(userTelegramId);
        message.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        message.setText(answer);

        messageExecutor.editMessage(message);
    }
}
