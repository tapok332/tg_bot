package com.example.tg_bot.service.handlers.informationhandler;

import com.example.tg_bot.service.InfoState;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithKeyboard;

@RequiredArgsConstructor
public class InfoHandler {

    private final InfoState infoState;
    private final UserData userData;

    public SendMessage handleInfo(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();
        Commands commandNow;

        switch (inputMessage) {
            case "My information" -> {
                return sendMessageWithKeyboard(INFO.getText(), message.getChatId(), getInfoKeyboard());
            }
            case "User information" -> commandNow = Commands.USER_INFO;
            case "Delivery information" -> commandNow = Commands.DELIVERY_INFO;
            case "Check all information" -> commandNow = Commands.CHECK_INFO;
            case "Menu" -> commandNow = Commands.MENU;
            default -> commandNow = userData.getUsersCurrentBotState(userId);
        }
        userData.saveUsersCurrentBotState(userId, commandNow);

        return infoState.processInputMessage(commandNow, message);
    }

    private ReplyKeyboardMarkup getInfoKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("User information"));
        firstRow.add(new KeyboardButton("Delivery information"));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton("Check all information"));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton("Menu"));
        KeyboardRow fourthRow = new KeyboardRow();
        fourthRow.add(new KeyboardButton("Help"));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);
        keyboard.add(fourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
