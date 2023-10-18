package com.example.tgbot.bot;

import com.example.tgbot.info.InfoHandler;
import com.example.tgbot.menu.MenuService;
import com.example.tgbot.order.OrderHandler;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

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
            case INFO, USER_INFO, SET_USER_INFO, UPDATE_USER_INFO, DELIVERY_INFO, SET_DELIVERY_INFO,
                    UPDATE_DELIVERY_INFO, CHECK_USER_INFO, CHECK_ALL_INFO, DELETE_USER_INFO -> {
                return infoHandler.handleInfo(message);
            }
            case BUY, LAPTOP_ORDERS, KEYBOARD_ORDERS -> {
                return orderHandler.handleOrder(message);
            }
            case HELP, MENU -> {
                return menuService.execute(message);
            }
            default -> {
                return sendMessage(textSender.getText(message.getFrom().getId(), "error_unknown"),
                        message.getChatId());
            }
        }
    }
}
