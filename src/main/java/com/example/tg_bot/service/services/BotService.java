package com.example.tg_bot.service.services;

import com.example.tg_bot.service.handlers.callback.CallBackHandler;
import com.example.tg_bot.service.handlers.messagehandler.MessageHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class BotService {
    private final CallBackHandler callBackHandler;
    private final MessageHandler messageHandler;

    public BotApiMethod<? extends Serializable> handleUpdate(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            return handleMessage(update.getMessage());
        }
        if (update.hasCallbackQuery()) {
            return handleCallbackQuery(update.getCallbackQuery());
        }
        return SendMessage.builder()
                .text("Unknown update.")
                .chatId(update.getMessage().getChatId())
                .build();
    }

    private BotApiMethod<? extends Serializable> handleCallbackQuery(CallbackQuery callbackQuery) {
        String username = callbackQuery.getFrom().getUserName();
        Long chatId = callbackQuery.getMessage().getChatId();

        log.info("New callbackQuery from User: {}, chatId: {} with data: {}", username, chatId, callbackQuery.getData());

        return callBackHandler.handleCallBack(callbackQuery);
    }

    private SendMessage handleMessage(Message message) {
        String username = message.getFrom().getUserName();
        Long chatId = message.getChatId();

        log.info("New message from User: {}, chatId: {},  with text: {}", username, chatId, message.getText());

        return messageHandler.handleMessage(message);
    }
}
