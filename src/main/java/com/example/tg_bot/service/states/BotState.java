package com.example.tg_bot.service.states;

import com.example.tg_bot.service.handlers.informationhandler.InfoHandler;
import com.example.tg_bot.service.handlers.ordershandler.OrderHandler;
import com.example.tg_bot.service.services.MenuService;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;

@Component
@RequiredArgsConstructor
public class BotState {
    private final InfoHandler infoHandler;
    private final OrderHandler orderHandler;
    private final MenuService menuService;

    private final TextSender textSender;

    public SendMessage processInputMessage(Commands currentState, Message message) {
        return findMessageHandler(currentState, message);
    }

    private SendMessage findMessageHandler(Commands currentState, Message message) {
        switch (currentState) {
            case INFO, USER_INFO, SET_USER_INFO, UPDATE_USER_INFO,
                    DELIVERY_INFO, SET_DELIVERY_INFO, UPDATE_DELIVERY_INFO, CHECK_USER_INFO, CHECK_ALL_INFO -> {
                return infoHandler.handleInfo(message);
            }
            case BUY, LAPTOP_ORDERS, KEYBOARD_ORDERS -> {
                return orderHandler.handleOrder(message);
            }
            case HELP, TEXT_PROCESSING -> {
                return menuService.execute(message);
            }
            default -> {
                return sendMessage(textSender.getText(message.getFrom().getId(), "error_unknown"),
                        message.getChatId());
            }
        }
    }
}
