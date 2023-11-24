package com.example.eventbot.services.commands;

import com.example.eventbot.bot.MessageExecutor;
import com.example.eventbot.exceptions.ExceptionDescriptor;
import com.example.eventbot.services.commands.impl.CommandStrategyRegistry;
import com.example.eventbot.utils.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class CommandService {
    private final CommandStrategyRegistry registry;

    private final MessageExecutor messageExecutor;

    public void invokeCommand(Command command, Message message) throws TelegramApiException {
        Message wait = messageExecutor.sendDefaultMessage("Подождите...", message);
        messageExecutor.sendDefaultAndDeletePrevious(
                registry.getHandlerForCommand(command)
                        .orElseThrow(ExceptionDescriptor.HANDLER_NOT_FOUND::exception)
                        .invokeCommand(message),
                wait
        );
    }
}
