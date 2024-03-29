package com.example.tgbot.bot;

import com.example.tgbot.handlers.CallBackHandler;
import com.example.tgbot.handlers.MessageHandler;
import com.example.tgbot.utils.exceptions.ShopException;
import com.example.tgbot.utils.text.TextSender;
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
    private final TextSender textSender;

    public BotApiMethod<? extends Serializable> handleUpdate(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                return handleMessage(update.getMessage());
            }
            if (update.hasCallbackQuery()) {
                return handleCallbackQuery(update.getCallbackQuery());
            }
        } catch (Exception e) {
            throw new ShopException("Something was wrong", e);
        }
        if (update.getCallbackQuery() == null) {
            return SendMessage.builder()
                    .text(textSender.getText(update.getMessage().getFrom().getId(), "error_unknown"))
                    .chatId(update.getMessage().getChatId())
                    .build();
        }else{
            return SendMessage.builder()
                    .text(textSender.getText(update.getCallbackQuery().getMessage().getFrom().getId(), "error_unknown"))
                    .chatId(update.getCallbackQuery().getMessage().getChatId())
                    .build();
        }
    }

    private BotApiMethod<? extends Serializable> handleCallbackQuery(CallbackQuery callbackQuery) {
        String username = callbackQuery.getFrom().getUserName();
        Long chatId = callbackQuery.getMessage().getChatId();
        log.debug("New callbackQuery from User: {}, chatId: {} with data: {}", username, chatId, callbackQuery.getData());

        return callBackHandler.handleCallBack(callbackQuery);
    }

    private SendMessage handleMessage(Message message) {
        String username = message.getFrom().getUserName();
        Long chatId = message.getChatId();
        log.debug("New message from User: {}, chatId: {},  with text: {}", username, chatId, message.getText());

        return messageHandler.handleMessage(message);
    }
}
