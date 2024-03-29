package com.example.tgbot.info;

import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.commands.CommandsHandler;
import com.example.tgbot.utils.text.TextSender;
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
    private final TextSender textSender;
    private final CommandsHandler commandsHandler;

    public SendMessage handleInfo(Message message) {
        String inputMessage = message.getText();
        Long userId = message.getFrom().getId();
        if (inputMessage.equals(textSender.getText(userId, "my_info"))
                || inputMessage.equals("without info")
                || inputMessage.equals("/start")) {
            return commandsHandler.info(message, getInfoKeyboard(userId));
        }
        Commands commandNow = commandsHandler.info(message);
        userData.saveUsersCurrentBotState(userId, commandNow);

        return infoState.processInputMessage(commandNow, message);
    }

    private ReplyKeyboardMarkup getInfoKeyboard(Long userId) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(false);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(textSender.getText(userId, "user_info")));
        firstRow.add(new KeyboardButton(textSender.getText(userId, "user_delivery_info")));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(textSender.getText(userId, "check_info")));
        secondRow.add(new KeyboardButton(textSender.getText(userId, "delete_info")));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton(textSender.getText(userId, "menu")));
        KeyboardRow fourthRow = new KeyboardRow();
        fourthRow.add(new KeyboardButton(textSender.getText(userId, "help")));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);
        keyboard.add(fourthRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
