package com.example.tg_bot.service.handlers.informationhandler;

import com.example.tg_bot.service.handlers.commandshandler.CommandsHandler;
import com.example.tg_bot.service.handlers.languagehandler.LanguageHandler;
import com.example.tg_bot.service.states.InfoState;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InfoHandler {

    private final InfoState infoState;
    private final UserData userData;
    private final LanguageHandler languageHandler;
    private final CommandsHandler commandsHandler;

    public SendMessage handleInfo(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();
        if (inputMessage.equals(languageHandler.getText(userId, "my_info"))
                || inputMessage.equals("error_info_handle")
        || inputMessage.equals("/start")) {
            return commandsHandler.info(message, getInfoKeyboard(userId));
        }
        Commands commandNow = commandsHandler.info(message);
        userData.saveUsersCurrentBotState(userId, commandNow);

        return infoState.processInputMessage(commandNow, message);
    }

    private ReplyKeyboardMarkup getInfoKeyboard(Long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(languageHandler.getText(userId, "user_info")));
        firstRow.add(new KeyboardButton(languageHandler.getText(userId, "user_delivery_info")));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(languageHandler.getText(userId, "check_info")));
        secondRow.add(new KeyboardButton(languageHandler.getText(userId, "delete_info")));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton(languageHandler.getText(userId, "menu")));
        KeyboardRow fourthRow = new KeyboardRow();
        fourthRow.add(new KeyboardButton(languageHandler.getText(userId, "help")));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);
        keyboard.add(fourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
