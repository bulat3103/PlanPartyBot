package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.model.enums.NotificationLevel;
import com.example.eventbot.services.UserService;
import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Answers;
import com.example.eventbot.utils.Command;
import com.example.eventbot.utils.KeyboardBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationCommandStrategy implements CommandStrategy {
    private final UserService userService;
    private final KeyboardBuilder keyboardBuilder;

    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command NOTIFICATIONS: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        //TODO проверить, что отправляет пользователь, который создал событие
        ExceptionDescriptor.INVALID_COMMAND.throwIfFalse(message.getChat().getType().equals("private"));
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));

        String answer = Answers.NOTIFICATION_CHOICE;
        SendMessage answerMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
        Map<String, String> buttons = new HashMap<>();
        buttons.put("1 раз в день", NotificationLevel.ONE_TIME_DAY.name());
        buttons.put("2 раза в день", NotificationLevel.TWO_TIME_DAY.name());
        buttons.put("3 раза в день", NotificationLevel.THREE_TIME_DAY.name());
        buttons.put("1 раз в 2 дня", NotificationLevel.ONE_TIME_TWO_DAY.name());
        buttons.put("1 раз в неделю", NotificationLevel.ONE_TIME_WEEK.name());
        answerMessage.setReplyMarkup(keyboardBuilder.getButtonList(buttons));
        return answerMessage;
    }

    @Override
    public Command getSupportedCommand() {
        return Command.NOTIFICATIONS;
    }
}
