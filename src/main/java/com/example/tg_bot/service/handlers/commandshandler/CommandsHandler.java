package com.example.tg_bot.service.handlers.commandshandler;

import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.sendmessage.Sending.sendMessageWithKeyboard;

@Component
@RequiredArgsConstructor
public class CommandsHandler {

    private final UserData userData;
    private final TextSender textSender;

    public SendMessage info(Message message, ReplyKeyboardMarkup keyboardMarkup) {
        if (message.getText().equals(textSender.getText(message.getFrom().getId(), "my_info"))) {
            return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "info"), message.getChatId(), keyboardMarkup);
        }
        if (message.getText().equals("without info")) {
            return sendMessageWithKeyboard(textSender.getText(message.getFrom().getId(), "error_user_check_info"), message.getChatId(), keyboardMarkup);
        }
        return sendMessage(textSender.getText(message.getFrom().getId(), "error_info_handle"), message.getChatId());
    }

    public Commands info(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();

        if (inputMessage.equals(textSender.getText(userId, "user_info"))) {
            return Commands.USER_INFO;
        }
        if (inputMessage.equals(textSender.getText(userId, "user_delivery_info"))) {
            return Commands.DELIVERY_INFO;
        }
        if (inputMessage.equals(textSender.getText(userId, "check_info"))) {
            return Commands.CHECK_USER_INFO;
        }
        if (inputMessage.equals(textSender.getText(userId, "delete_info"))) {
            return Commands.DELETE_USER_INFO;
        }

        return getMenuOrHelp(inputMessage, userId);
    }

    public Commands message(String inputMessage, Long userId) {
        if (inputMessage.equals(textSender.getText(userId, "go_buy"))) {
            return Commands.BUY;
        }
        if (inputMessage.equals(textSender.getText(userId, "my_info"))) {
            return Commands.INFO;
        }

        return getMenuOrHelp(inputMessage, userId);
    }

    private Commands getMenuOrHelp(String inputMessage, Long userId) {
        if (inputMessage.equals(textSender.getText(userId, "menu"))) {
            return Commands.TEXT_PROCESSING;
        }
        if (inputMessage.equals(textSender.getText(userId, "help"))) {
            return Commands.HELP;
        }
        return userData.getUsersCurrentBotState(userId);
    }
}
