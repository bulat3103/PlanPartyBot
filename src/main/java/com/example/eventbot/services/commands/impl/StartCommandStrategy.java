package com.example.eventbot.services.commands.impl;

import com.example.eventbot.exceptions.ApplicationException;
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
public class StartCommandStrategy implements CommandStrategy {
    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command START: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        String answer;
        return SendMessage
                .builder()
                .chatId(message.getChatId())
                .text(answer)
                .build();
    }

    @Override
    public Command getSupportedCommand() {
        return Command.START;
    }
}
