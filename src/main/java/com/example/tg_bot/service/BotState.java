package com.example.tg_bot.service;

import com.example.tg_bot.service.handlers.informationhandler.InfoHandler;
import com.example.tg_bot.service.handlers.ordershandler.OrderHandler;
import com.example.tg_bot.utils.commands.Commands;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.text.en.TextsForMessage.INCORRECT_COMMAND;

@Component
@RequiredArgsConstructor
public class BotState {
    private final InfoHandler infoHandler;
    private final OrderHandler orderHandler;

    public SendMessage processInputMessage(Commands currentState, Message message) {
        return findMessageHandler(currentState, message);
    }

    private SendMessage findMessageHandler(Commands currentState, Message message) {
        switch (currentState) {
            case INFO, USER_INFO, SET_USER_INFO, UPDATE_USER_INFO,
                    DELIVERY_INFO, SET_DELIVERY_INFO, UPDATE_DELIVERY_INFO, CHECK_INFO -> {
                return infoHandler.handleInfo(message);
            }
            case BUY -> {
                return orderHandler.handleOrder(message);
            }
            default -> {
                return sendMessage(INCORRECT_COMMAND.getText(), message.getChatId());
            }
        }
    }
}
