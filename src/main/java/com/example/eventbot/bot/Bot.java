package com.example.eventbot.bot;

import com.example.eventbot.configs.BotConfig;
import com.example.eventbot.exceptions.ApplicationException;
import com.example.eventbot.model.Event;
import com.example.eventbot.model.dto.CreateEventDto;
import com.example.eventbot.services.ActionService;
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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final MessageParser messageParser;
    private final ActionService actionService;
    private final CommandService commandService;
    private final BotConfig botConfig;
    private final Map<Long, Command> previousUserCommand = new HashMap<>();
    private final Map<Long, CreateEventDto> userEventDto = new HashMap<>();
    private final Map<Long, Event> wishEvent = new HashMap<>();

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        log.info("new update received: ({})", update.toString());
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                if (messageParser.hasCommand(text)) {
                    Command command = messageParser.checkCommand(text);
                    previousUserCommand.put(update.getMessage().getFrom().getId(), command);
                    commandService.invokeCommand(command, update.getMessage());
                } else {
                    if (messageParser.hasCode(text)
                            && previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.JOIN))
                    {
                        actionService.joinChatByCode(update.getMessage());
                    } else if (messageParser.hasCode(text)
                            && previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.EVENT))
                    {
                        CreateEventDto dto = new CreateEventDto();
                        dto.setChatCode(Integer.valueOf(text.trim()));
                        userEventDto.put(update.getMessage().getFrom().getId(), dto);
                        execute(SendMessage.builder()
                                .chatId(update.getMessage().getChatId())
                                .text("Введите дату события")
                                .build());
                    } else if (messageParser.hasDate(text)
                            && previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.EVENT))
                    {
                        CreateEventDto dto = userEventDto.get(update.getMessage().getFrom().getId());
                        dto.setDate(text);
                        userEventDto.put(update.getMessage().getFrom().getId(), dto);
                        execute(SendMessage.builder()
                                .chatId(update.getMessage().getChatId())
                                .text("Введите название события и список дел (каждое значение с новой строчки)")
                                .build());
                    } else if (previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.EVENT)) {
                        CreateEventDto dto = userEventDto.get(update.getMessage().getFrom().getId());
                        String[] split = text.split("\n", 2);
                        dto.setName(split[0]);
                        dto.setListOfWork(split[1]);
                        userEventDto.remove(update.getMessage().getFrom().getId());
                        actionService.createEventByChatCode(update.getMessage(), dto);
                    } else if (previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.WISH)
                            && messageParser.hasCode(text))
                    {
                        Event event = actionService.getAllTasksForEvent(update.getMessage());
                        wishEvent.put(update.getMessage().getFrom().getId(), event);
                    } else if (previousUserCommand.get(update.getMessage().getFrom().getId()).equals(Command.WISH)) {
                        actionService.setWish(update.getMessage(), wishEvent.get(update.getMessage().getFrom().getId()));
                        wishEvent.remove(update.getMessage().getFrom().getId());
                    }
                }
            } else if (update.hasCallbackQuery()) {
                String callBack = update.getCallbackQuery().getData();
                if (messageParser.checkForNotification(callBack)) {

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
