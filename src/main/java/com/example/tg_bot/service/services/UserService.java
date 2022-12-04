package com.example.tg_bot.service.services;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.service.handlers.userhandler.processing.UserProcessing;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.utilforsendmessage.Sending;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;


@Service
@Component
@RequiredArgsConstructor
public class UserService implements DefaultService {

    private final UserData userData;
    private final UserProcessing userProcessing;

    @Override
    public SendMessage handle(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.USER_INFO)) {
            return Sending.sendMessageWithButton(INFO.getText(), message.getChatId(), getUserButtons());
        }

        return processingUserInfo(message);
    }

    @Override
    public Commands getHandlerName() {
        return Commands.USER_INFO;
    }

    private SendMessage processingUserInfo(Message message) {
        Commands botState = userData.getUsersCurrentBotState(message.getFrom().getId());

        switch (botState) {
            case SET_USER_INFO -> {
                return userProcessing.saveInfo(message);
            }
            case UPDATE_USER_INFO -> {
                return userProcessing.updateInfo(message);
            }
        }

        return sendMessage("thx", message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getUserButtons() {
        InlineKeyboardButton setInfoButton = InlineKeyboardButton.builder()
                .text("Set my info")
                .callbackData("set_user")
                .build();
        InlineKeyboardButton updateInfoButton = InlineKeyboardButton.builder()
                .text("Update my info")
                .callbackData("update_user")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(setInfoButton, updateInfoButton);

        return List.of(keyboard);
    }
}
