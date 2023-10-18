package com.example.tgbot.delivery;

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
public class UserDeliveryService implements DefaultService {

    private final UserData userData;
    private final DeliveryProcessing deliveryProcessing;
    private final TextSender textSender;

    @Override
    public SendMessage execute(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.DELIVERY_INFO)) {
            return Sending.sendMessageWithButton(textSender.getText(message.getFrom().getId(), "info"),
                    message.getChatId(), getDeliveryButtons(message.getFrom().getId()));
        }
        return processingDeliveryInfo(message);
    }

    @Override
    public Commands getExecuteName() {
        return Commands.DELIVERY_INFO;
    }

    private SendMessage processingDeliveryInfo(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.SET_DELIVERY_INFO)) {
            return deliveryProcessing.saveInfo(message);
        }
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.UPDATE_DELIVERY_INFO)) {
            deliveryProcessing.updateInfo(message);
        }
        return sendMessage(textSender.getText(message.getFrom().getId(), "error_unknown"),
                message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getDeliveryButtons(@NonNull Long userId) {
        InlineKeyboardButton setInfoButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "set_user_info"))
                .callbackData("set_address")
                .build();
        InlineKeyboardButton updateInfoButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "update_user_info"))
                .callbackData("update_address")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(setInfoButton, updateInfoButton);

        return List.of(keyboard);
    }
}
