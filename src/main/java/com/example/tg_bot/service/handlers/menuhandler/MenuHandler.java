package com.example.tg_bot.service.handlers.menuhandler;

import com.example.tg_bot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessageWithKeyboard;

@Component
@RequiredArgsConstructor
public class MenuHandler {
    private final TextSender textSender;
    private static final String CHOOSE_LANGUAGE = """
        Please choose the language:
        English or Ukrainian
        
        Будь-ласка виберіть мову:
        Англійська або Українська""";

    public SendMessage handleMenu(Message message) {
        return sendMessageWithKeyboard(CHOOSE_LANGUAGE, message.getChatId(), getChooseLanguageKeyboard());
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
        return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "menu_message"),
                message.getChatId(), getStartKeyboard(message.getFrom().getId()));
    }


    private ReplyKeyboardMarkup getStartKeyboard(Long userId) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(textSender.getText(userId, "go_buy")));
        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(textSender.getText(userId, "my_info")));
        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton(textSender.getText(userId, "help")));
        keyboard.add(firstRow);
        keyboard.add(secondRow);
        keyboard.add(thirdRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
