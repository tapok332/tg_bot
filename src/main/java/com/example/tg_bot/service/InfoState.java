package com.example.tg_bot.service;

import com.example.tg_bot.utils.commands.Commands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INCORRECT_COMMAND;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

@Component
public class InfoState {

    private final Map<Commands, DefaultService> messageHandlers = new HashMap<>();

    public InfoState(List<DefaultService> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(Commands currentState, Message message) {
        DefaultService currentMessageHandler = findMessageHandler(currentState);
        if (currentMessageHandler == null) {
            return sendMessage(INCORRECT_COMMAND.getText(), message.getChatId());
        }
        return currentMessageHandler.handle(message);
    }

    private DefaultService findMessageHandler(Commands currentState) {
        switch (currentState) {
            case USER_INFO, SET_USER_INFO, UPDATE_USER_INFO -> {
                return messageHandlers.get(Commands.USER_INFO);
            }
            case DELIVERY_INFO, SET_DELIVERY_INFO, UPDATE_DELIVERY_INFO -> {
                return messageHandlers.get(Commands.DELIVERY_INFO);
            }
            case CHECK_INFO -> {
                return messageHandlers.get(Commands.ALL_INFO);
            }
            default -> {
                return messageHandlers.get(currentState);
            }
        }
    }
}
