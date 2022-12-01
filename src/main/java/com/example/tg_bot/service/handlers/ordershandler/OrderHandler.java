package com.example.tg_bot.service.handlers.ordershandler;

import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.utils.utilforsendmessage.Sending;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.tg_bot.utils.text.en.TextsForMessage.INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessageWithButton;

@RequiredArgsConstructor
public class OrderHandler {

    public SendMessage handleOrder(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();
        Commands commandNow;

        switch (inputMessage) {
            case "Go to buy" -> {
                return sendMessageWithButton(INFO.getText(), message.getChatId(), getOrderButtons());
            }
            case "User information" -> commandNow = Commands.USER_INFO;
            case "Delivery information" -> commandNow = Commands.DELIVERY_INFO;
            case "Check all information" -> commandNow = Commands.CHECK_INFO;
            case "Menu" -> commandNow = Commands.MENU;
            default -> commandNow = userData.getUsersCurrentBotState(userId);
        }

        return sendMessageWithButton("OrderHandler", message.getChatId(), getOrderButtons());
    }

    private List<List<InlineKeyboardButton>> getOrderButtons() {
        InlineKeyboardButton firstType = InlineKeyboardButton.builder()
                .text("Hair cosmetics")
                .callbackData("hair")
                .build();
        InlineKeyboardButton secondType = InlineKeyboardButton.builder()
                .text("Face cosmetics")
                .callbackData("face")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(firstType, secondType);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        buttons.add(keyboard);

        return buttons;
    }
}
