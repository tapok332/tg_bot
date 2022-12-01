package com.example.tg_bot.service.handlers.orderpageshandler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendPages;

public class OrderPagesHandler {

    private List<List<InlineKeyboardButton>> buttons;

    public SendMessage handlePages(CallbackQuery callbackQuery){
        Long chatId = callbackQuery.getMessage().getChatId();
        int pageNumber = Integer.parseInt(callbackQuery.getData().substring(5));

        switch (pageNumber){
            case 1 -> {
                return sendPages(getFirstPage(pageNumber), buttons, chatId);
            }
            case 2 -> {
                return getSecondPage();
            }
            case 3 -> {
                return getThirdPage();
            }
            default -> sendMessage("OrderPagesHandler error!", chatId);
        }
    }

    private EditMessageText getFirstPage(int pageNumber) {
        InlineKeyboardButton firstPage = InlineKeyboardButton.builder()
                .text(">")
                .callbackData(String.valueOf(pageNumber + 1))
                .build();
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(String.valueOf(pageNumber + 1))
                .build();
        buttons = List.of(List.of(countPage, firstPage));


    }

    private SendMessage getSecondPage() {
        return null;
    }

    private SendMessage getThirdPage() {
        return null;
    }
}
