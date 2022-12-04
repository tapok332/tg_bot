package com.example.tg_bot.service.services;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.service.handlers.deliveryhandler.processing.DeliveryProcessing;
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

@Service
@Component
@RequiredArgsConstructor
public class UserDeliveryService implements DefaultService {

    private final UserData userData;
    private final DeliveryProcessing deliveryProcessing;

    @Override
    public SendMessage handle(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.DELIVERY_INFO)) {
            return Sending.sendMessageWithButton(INFO.getText(), message.getChatId(), getDeliveryButtons());
        }
        return processingDeliveryInfo(message);
    }

    @Override
    public Commands getHandlerName() {
        return Commands.DELIVERY_INFO;
    }

    private SendMessage processingDeliveryInfo(Message message) {
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.SET_DELIVERY_INFO)) {
            return deliveryProcessing.saveInfo(message);
        }
        if (userData.getUsersCurrentBotState(message.getFrom().getId()).equals(Commands.UPDATE_DELIVERY_INFO)) {
            deliveryProcessing.updateInfo(message);
        }
        return Sending.sendMessage("thx", message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getDeliveryButtons() {
        InlineKeyboardButton setInfoButton = InlineKeyboardButton.builder()
                .text("Set my info")
                .callbackData("set_address")
                .build();
        InlineKeyboardButton updateInfoButton = InlineKeyboardButton.builder()
                .text("Update my info")
                .callbackData("update_address")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(updateInfoButton, setInfoButton);

        return List.of(keyboard);
    }
}
