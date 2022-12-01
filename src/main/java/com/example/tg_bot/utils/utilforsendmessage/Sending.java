package com.example.tg_bot.utils.utilforsendmessage;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Sending {

    public static SendMessage sendMessage(String textForSend, Long id) {
        return SendMessage.builder()
                .text(textForSend)
                .chatId(id)
                .build();
    }

    public static SendMessage sendMessageWithButton(String text, Long chatId, List<List<InlineKeyboardButton>> button) {
        return SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(button)
                        .build())
                .build();
    }

    public static SendMessage sendMessageWithKeyboard(String text, Long chatId, ReplyKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .replyMarkup(keyboard)
                .build();
    }

    public static SendMessage sendPages(EditMessageText text, Long chatId, ReplyKeyboardMarkup keyboard) {
        return SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .replyMarkup(keyboard)
                .build();
    }
}
