package com.example.eventbot.bot;

import com.example.eventbot.configs.BotConfig;
import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.services.commands.CommandService;
import com.example.eventbot.utils.Command;
import com.example.eventbot.utils.MessageParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final MessageParser messageParser;
    private final CommandService commandService;
    private final BotConfig botConfig;

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info("new update received: ({})", update.toString());
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                if (messageParser.hasCommand(text)) {
                    Command command = messageParser.checkCommand(text);
                    commandService.invokeCommand(command, update.getMessage());
                } else {
                    if (messageParser.hasCode(text)) {

                    }
                }
            }
        } catch (ApplicationException e) {
            log.info("catch exception: ({})", e.getException().getMessage());
            execute(SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(e.getException().getResponse())
                    .build());
        }
    }

    @Override
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) throws TelegramApiException {
        return super.execute(method);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
