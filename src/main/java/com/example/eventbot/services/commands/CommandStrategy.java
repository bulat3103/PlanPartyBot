package com.example.eventbot.services.commands;

import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.utils.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface CommandStrategy {
    SendMessage invokeCommand(Message message) throws ApplicationException;

    Command getSupportedCommand();
}
