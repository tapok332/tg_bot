package com.example.tg_bot.service.services;

import com.example.tg_bot.service.DefaultService;
import com.example.tg_bot.service.handlers.deliveryhandler.processing.DeliveryProcessing;
import com.example.tg_bot.service.handlers.userhandler.processing.UserProcessing;
import com.example.tg_bot.utils.commands.Commands;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tg_bot.utils.text.en.TextsForMessage.DELETED_INFO;
import static com.example.tg_bot.utils.utilforsendmessage.Sending.sendMessage;

@Service
@Component
@RequiredArgsConstructor
public class InfoService  implements DefaultService {

    private final UserProcessing userProcessing;
    private final DeliveryProcessing deliveryProcessing;

    @Override
    public SendMessage handle(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        if(message.getText().equals("Delete my information")){

            return deliveryProcessing.deleteAllInfo(userId, chatId);
        } else {
            return sendMessage(userProcessing.checkInfo(userId) +
                    "\n\n" +
                    deliveryProcessing.checkInfo(userId), chatId);
        }
    }

    @Override
    public Commands getHandlerName() {
        return Commands.ALL_INFO;
    }
}
