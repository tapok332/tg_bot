package com.example.tg_bot.service.handlers.ordershandler;

import com.example.tg_bot.service.handlers.informationhandler.InfoHandler;
import com.example.tg_bot.utils.text.TextSender;
import com.example.tg_bot.service.handlers.ordershandler.orderprocessing.OrderProcessing;
import com.example.tg_bot.service.states.OrderState;
import com.example.tg_bot.utils.cache.UserData;
import com.example.tg_bot.utils.commands.Commands;
import com.example.tg_bot.validation.UserValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.tg_bot.utils.sendmessage.Sending.sendMessage;
import static com.example.tg_bot.utils.sendmessage.Sending.sendMessageWithButton;

@Component
@RequiredArgsConstructor
public class OrderHandler {

    private final OrderState orderState;
    private final UserData userData;
    private final UserValidate userValidate;
    private final InfoHandler infoHandler;
    private final OrderProcessing orderProcessing;
    private final TextSender textSender;

    public SendMessage handleOrder(Message message) {
        String inputMessage = message.getText();
        Long userId = message
                .getFrom()
                .getId();
        Commands commandNow = userData.getUsersCurrentBotState(userId);

        if (inputMessage.equals(textSender.getText(userId, "go_buy"))) {
            return sendMessageWithButton(textSender.getText(userId, "orders_info"), message.getChatId(), getOrderButtons(userId));
        }
        if (inputMessage.equals(textSender.getText(userId, "menu_message"))) {
            commandNow = Commands.TEXT_PROCESSING;
        }

        return orderState.processInputMessage(commandNow, message);
    }

    public SendMessage handleOrderInfo(Message message, String item) {
        if (item.contains("false")) {
            return sendMessage(textSender.getText(message.getFrom().getId(), "error_no_in_stock"), message.getChatId());
        } else {

            return validateUser(message, item.substring(12, 13));
        }
    }

    private SendMessage validateUser(Message message, String itemId) {
        Long userId = message.getFrom().getId();
        if (!userValidate.hasUser(userId) || !userValidate.hasUserAddress(userId)) {
            message.setText("without info");

            return infoHandler.handleInfo(message);
        }
        if (userValidate.hasUser(userId) && userValidate.hasUserAddress(userId)) {
            String item = orderProcessing.getOrderInfo(message.getFrom().getId(), itemId);
            message.setText(textSender.getText(userId, "check_info"));

            String userInfo = infoHandler.handleInfo(message).getText();

            return sendMessage(item + "\n\n" + userInfo, message.getChatId());
        }
        return sendMessage(textSender.getText(userId, "error_validate"), message.getChatId());
    }

    private List<List<InlineKeyboardButton>> getOrderButtons(Long userId) {
        InlineKeyboardButton laptopsButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "laptops"))
                .callbackData("laptops")
                .build();
        InlineKeyboardButton keyboardsButton = InlineKeyboardButton.builder()
                .text(textSender.getText(userId, "keyboards"))
                .callbackData("keyboards")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(laptopsButton, keyboardsButton);

        return List.of(keyboard);
    }
}
