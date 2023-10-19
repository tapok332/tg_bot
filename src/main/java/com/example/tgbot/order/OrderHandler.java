package com.example.tgbot.order;

import com.example.tgbot.info.InfoHandler;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import com.example.tgbot.validation.UserValidate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;
import static com.example.tgbot.utils.sendmessage.Sending.sendMessageWithButton;

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
        var inputMessage = message.getText();
        var userId = message.getFrom().getId();

        if (inputMessage.equals(textSender.getText(userId, "go_buy"))) {
            return sendMessageWithButton(textSender.getText(userId, "orders_info"), message.getChatId(), getOrderButtons(userId));
        }
        if (inputMessage.equals(textSender.getText(userId, "menu_message"))) {
            return orderState.processInputMessage(Commands.MENU, message);
        }
        return orderState.processInputMessage(userData.getUsersCurrentBotState(userId), message);
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
        if (!userValidate.isUserValid(userId)) {
            message.setText("without info");

            return infoHandler.handleInfo(message);
        } else {
            String item = orderProcessing.getOrderInfo(message.getFrom().getId(), itemId);
            message.setText(textSender.getText(userId, "check_info"));

            String userInfo = infoHandler.handleInfo(message).getText();

            return sendMessage(item + "\n\n" + userInfo, message.getChatId());
        }
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
