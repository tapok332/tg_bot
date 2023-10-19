package com.example.tgbot.handlers;

import com.example.tgbot.order.OrderPagesHandler;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.validation.UserValidate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class CallBackHandler {

    private final UserValidate userValidate;
    private final UserData userData;
    private final OrderPagesHandler orderPagesHandler;

    public BotApiMethod<? extends Serializable> handleCallBack(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        String callbackQueryData = callbackQuery.getData();

        if (callbackQueryData.contains("page")) {
            return orderPagesHandler.handlePages(callbackQuery);
        }
        return switch (callbackQueryData) {
            case "set_user" -> userValidate.setUserInfo(userId, chatId);
            case "set_address" -> userValidate.setUserAddress(userId, chatId);
            case "update_user" -> userValidate.updateUserInfo(userId, chatId);
            case "update_address" -> userValidate.updateUserAddress(userId, chatId);
            case "laptops" -> getLaptopItems(userId, callbackQuery);
            case "keyboards" -> getKeyboardsItems(userId, callbackQuery);
            default -> sendMessage("Unknown request.", chatId);
        };
    }

    private BotApiMethod<? extends Serializable> getLaptopItems(Long userId, CallbackQuery callbackQuery) {
        log.debug("Laptops information for user = {}", userId);
        userData.saveUsersCurrentBotState(userId, Commands.LAPTOP_ORDERS);
        return orderPagesHandler.handlePages(callbackQuery);
    }

    private BotApiMethod<? extends Serializable> getKeyboardsItems(Long userId, CallbackQuery callbackQuery) {
        log.debug("Keyboards information for user = {}", userId);
        userData.saveUsersCurrentBotState(userId, Commands.KEYBOARD_ORDERS);
        return orderPagesHandler.handlePages(callbackQuery);
    }
}
