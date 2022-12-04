package com.example.tg_bot.service.handlers.callback;

import com.example.tg_bot.service.handlers.orderpageshandler.OrderPagesHandler;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.validation.UserValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

import static com.example.tg_bot.utils.text.en.TextsForMessage.*;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

@Component
@RequiredArgsConstructor
public class CallBackHandler {

    private final UserValidate userValidate;
    private final UserData userData;
    private final OrderPagesHandler orderPagesHandler;

    public BotApiMethod<? extends Serializable> handleCallBack(CallbackQuery callbackQuery) {
        Long userId = callbackQuery.getFrom().getId();
        Long chatId = callbackQuery.getMessage().getChatId();
        BotApiMethod<? extends Serializable> replyMessage = sendMessage("Unknown request.", chatId);
        String callbackQueryData = callbackQuery.getData();

        if (callbackQueryData.contains("page")) {
            return orderPagesHandler.handlePages(callbackQuery);
        }
        switch (callbackQueryData) {
            case "set_user" -> replyMessage = setUserInfo(userId, chatId);
            case "set_address" -> replyMessage = setUserAddress(userId, chatId);
            case "update_user" -> replyMessage = updateUserInfo(userId, chatId);
            case "update_address" -> replyMessage = updateUserAddress(userId, chatId);
            case "laptops" -> replyMessage = getLaptopItems(userId, callbackQuery);
            case "keyboards" -> replyMessage = getKeyboardsItems(userId, callbackQuery);
        }

        return replyMessage;
    }

    private SendMessage setUserInfo(Long userId, Long chatId) {
        if (userValidate.hasUser(userId)) {
            return sendMessage(ERROR_USER_INFO.getText(), chatId);
        } else {
            userData.saveUsersCurrentBotState(userId, Commands.SET_USER_INFO);
            return sendMessage("Please send your name:", chatId);
        }
    }

    private SendMessage setUserAddress(Long userId, Long chatId) {
        if (userValidate.hasUser(userId) && !userValidate.hasUserAddress(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.SET_DELIVERY_INFO);
            return sendMessage("Please send your country:", chatId);
        } else {
            return sendMessage(ERROR_DELIVERY_INFO.getText(), chatId);
        }
    }

    private SendMessage updateUserInfo(Long userId, Long chatId) {
        if (userValidate.hasUser(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.UPDATE_USER_INFO);
            return sendMessage("Please send your new name:", chatId);
        } else {
            return sendMessage(ERROR_UPDATE_USER_INFO.getText(), chatId);
        }
    }

    private SendMessage updateUserAddress(Long userId, Long chatId) {
        if (userValidate.hasUserAddress(userId) && userValidate.hasUser(userId)) {
            userData.saveUsersCurrentBotState(userId, Commands.UPDATE_DELIVERY_INFO);
            return sendMessage("Please send your new country:", chatId);
        } else {
            if (!userValidate.hasUser(userId)) {
                return sendMessage(ERROR_UPDATE_DELIVERY_INFO_BY_USER.getText(), chatId);
            }
            if (!userValidate.hasUserAddress(userId)) {
                return sendMessage(ERROR_UPDATE_DELIVERY_INFO_BY_ADDRESS.getText(), chatId);
            }
        }
        return sendMessage("Error processing your choose.", chatId);
    }

    private BotApiMethod<? extends Serializable> getLaptopItems(Long userId, CallbackQuery callbackQuery) {
        userData.saveUsersCurrentBotState(userId, Commands.LAPTOP_ORDERS);
        return orderPagesHandler.handlePages(callbackQuery);
    }

    private BotApiMethod<? extends Serializable> getKeyboardsItems(Long userId, CallbackQuery callbackQuery) {
        userData.saveUsersCurrentBotState(userId, Commands.KEYBOARD_ORDERS);
        return orderPagesHandler.handlePages(callbackQuery);
    }
}
