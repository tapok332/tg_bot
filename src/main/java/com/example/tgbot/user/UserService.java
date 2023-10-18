package com.example.tgbot.user;

import com.example.tgbot.service.DefaultService;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.sendmessage.Sending;
import com.example.tgbot.utils.text.TextSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;


@Service
@Component
@RequiredArgsConstructor
public class UserService implements DefaultService {

    private final UserData userData;
    private final UserProcessing userProcessing;
    private final TextSender textSender;

    @Override
    public SendMessage execute(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.USER_INFO)) {
            return Sending.sendMessageWithButton(textSender.getText(message.getFrom().getId(), "info"),
                    message.getChatId(), getUserButtons(message.getFrom().getId()));
        }
        return processingUserInfo(message);
    }

    @Override
    public Commands getExecuteName() {
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

        return sendMessage(textSender.getText(message.getFrom().getId(), "error_unknown"),
                message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getUserButtons(@NonNull Long userId) {
        InlineKeyboardButton setInfoButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "set_user_info"))
                .callbackData("set_user")
                .build();
        InlineKeyboardButton updateInfoButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "update_user_info"))
                .callbackData("update_user")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(setInfoButton, updateInfoButton);

        return List.of(keyboard);
    }
}
