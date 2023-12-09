package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.services.ChatService;
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
public class GetChatCodeCommandStrategy implements CommandStrategy {
    private final ChatService chatService;
    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command GET_CHAT_CODE: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        ExceptionDescriptor.INVALID_COMMAND.throwIfTrue(message.getChat().getType().equals("private"));
        String answer = "Код чата: " + chatService.getChat(message.getChatId()).getCode();
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.CODE;
    }
}
