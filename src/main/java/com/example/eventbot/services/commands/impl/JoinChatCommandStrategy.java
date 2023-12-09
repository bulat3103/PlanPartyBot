package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.services.UserService;
import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class JoinChatCommandStrategy implements CommandStrategy {
    private final UserService userService;
    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command JOIN_CHAT: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.INVALID_COMMAND.throwIfFalse(message.getChat().getType().equals("private"));
        String answer;
        if (!userService.checkUser(message.getFrom().getId())) {
            answer = "Бот о тебе ничего не знает:(\nВведи команду /start для регистрации";
        } else {
            answer = "Введи четырехзначный код из чата!";
        }
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.JOIN;
    }
}
