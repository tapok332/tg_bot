package com.example.tg_bot.service.handlers.menuhandler;

import com.example.tg_bot.utils.cache.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.example.tg_bot.utils.text.en.TextsForMessage.CHOOSE_LANGUAGE;
import static com.example.tg_bot.utils.text.en.TextsForMessage.MENU;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithKeyboard;

@Component
@RequiredArgsConstructor
public class MenuHandler {

    private final UserData userData;

    public SendMessage handleMenu(Message message) {
        return sendMessageWithKeyboard(CHOOSE_LANGUAGE.getText(), message.getChatId(), getChooseLanguageKeyboard());
    }

    private ReplyKeyboardMarkup getChooseLanguageKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("Ukrainian - Українська"));
        firstRow.add(new KeyboardButton("English - Англійська"));
        keyboard.add(firstRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    public SendMessage sendMenu(Message message) {
        return sendMessageWithKeyboard(MENU.getText(), message.getChatId(), getStartKeyboard(message.getFrom().getId()));
    }


    private ReplyKeyboardMarkup getStartKeyboard(Long userId) {
        Locale userLanguage = userData.getUsersLanguage(userId) == null ? new Locale("en", "EN")
                : userData.getUsersLanguage(userId);
        ResourceBundle bundle = ResourceBundle.getBundle("texts.text", userLanguage);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(bundle.getString("go_buy")));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(bundle.getString("my_info")));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton(bundle.getString("help")));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
