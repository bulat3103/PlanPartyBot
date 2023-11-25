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
public class CreateEventCommandStrategy implements CommandStrategy {
    @Override
    public SendMessage invokeCommand(Message message) throws ApplicationException {
        log.info("invoke command CREATE_EVENT: ({}, {})", message.getChatId(), message.getFrom().getUserName());
        return null;
    }

    @Override
    public Command getSupportedCommand() {
        return Command.CREATE_EVENT;
    }
}
