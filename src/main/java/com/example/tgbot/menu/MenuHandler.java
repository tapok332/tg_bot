package com.example.tgbot.menu;

import com.example.tgbot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessageWithKeyboard;

@Component
@RequiredArgsConstructor
public class MenuHandler {
    private final TextSender textSender;

    public SendMessage handleMenu(Message message) {
        if (message.getText().equals("/start")) {
            return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "start"), message.getChatId(), getChooseLanguageKeyboard());
        }
        return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "choose_language"), message.getChatId(), getChooseLanguageKeyboard());
    }

    public SendMessage sendMenu(Message message) {
        return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "menu_message"),
                message.getChatId(), getStartKeyboard(message.getFrom().getId()));
    }

    private ReplyKeyboardMarkup getChooseLanguageKeyboard() {
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton("Ukrainian - Українська"));
        firstRow.add(new KeyboardButton("English - Англійська"));

        return getReplyKeyboardMarkup(List.of(firstRow));
    }

    private ReplyKeyboardMarkup getStartKeyboard(Long userId) {
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add(new KeyboardButton(textSender.getText(userId, "go_buy")));

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add(new KeyboardButton(textSender.getText(userId, "my_info")));

        KeyboardRow thirdRow = new KeyboardRow();
        thirdRow.add(new KeyboardButton(textSender.getText(userId, "help")));

        return getReplyKeyboardMarkup(List.of(firstRow, secondRow, thirdRow));
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(List<KeyboardRow> keyboard) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
