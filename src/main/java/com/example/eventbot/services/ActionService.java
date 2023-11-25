package com.example.eventbot.services;

import com.example.eventbot.bot.MessageExecutor;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActionService {
    private final UserService userService;
    private final ChatService chatService;
    private final MessageExecutor messageExecutor;

    public void joinChatByCode(Message message) throws TelegramApiException {
        log.info("invoke joinChatByCode: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.NO_INFO_ERROR.throwIfFalse(userService.checkUser(message.getFrom().getId()));
        ExceptionDescriptor.JOIN_ERROR.throwIfFalse(chatService.checkCode(Integer.valueOf(message.getText().trim())));
        String answer = "Спасибо за присоединение!";

        messageExecutor.sendDefaultMessage(SendMessage.builder()
                .chatId(message.getChatId())
                .text(answer)
                .build());
    }
}
