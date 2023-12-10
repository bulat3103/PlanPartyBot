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
        ExceptionDescriptor.INVALID_COMMAND.throwIfTrue(message.getChat().getType().equals("private"));
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));

        String answer = Answers.NOTIFICATION_CHOICE;
        SendMessage answerMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
        Map<String, String> buttons = new HashMap<>();
        buttons.put("Раз в день", NotificationLevel.DAY.name());
        buttons.put("Раз в 2 дня", NotificationLevel.TWO_DAY.name());
        buttons.put("Раз в 3 дня", NotificationLevel.THREE_DAY.name());
        buttons.put("Раз в неделю", NotificationLevel.WEEK.name());
        answerMessage.setReplyMarkup(keyboardBuilder.getButtonList(buttons));
        return answerMessage;
    }

    @Override
    public Command getSupportedCommand() {
        return Command.NOTIFICATIONS;
    }
}
