package com.example.eventbot.services.commands.impl;

import com.example.eventbot.services.commands.CommandStrategy;
import com.example.eventbot.utils.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class CommandStrategyRegistry {
    private final Map<Command, CommandStrategy> commandStrategyMap;

    public CommandStrategyRegistry(@Autowired Set<CommandStrategy> commandStrategies) {
        Map<Command, CommandStrategy> commandStrategyMap = new HashMap<>();
        commandStrategies.forEach(command -> commandStrategyMap.put(command.getSupportedCommand(), command));
        this.commandStrategyMap = commandStrategyMap;
    }

    public Optional<CommandStrategy> getHandlerForCommand(Command command) {
        return Optional.ofNullable(commandStrategyMap.get(command));
    }
}
