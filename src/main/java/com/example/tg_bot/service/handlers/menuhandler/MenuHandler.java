package com.example.tg_bot.service.handlers.menuhandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithKeyboard;

@Component
public class MenuHandler {
    public SendMessage handleMenu(Message message) {
        return sendMessageWithKeyboard("You in menu", message.getChatId(), getStartKeyboard());
    }

    private ReplyKeyboardMarkup getStartKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("Go to buy"));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton("My information"));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton("Help"));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
