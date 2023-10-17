package com.example.tgbot.info;

import com.example.tgbot.delivery.DeliveryProcessing;
import com.example.tgbot.service.DefaultService;
import com.example.tgbot.user.UserProcessing;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.text.TextSender;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

@Service
@Component
@RequiredArgsConstructor
public class InfoService  implements DefaultService {

    private final UserProcessing userProcessing;
    private final DeliveryProcessing deliveryProcessing;
    private final TextSender textSender;

    @Override
    public SendMessage execute(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        if(message.getText().equals(textSender.getText(userId, "delete_info"))){
            return deliveryProcessing.deleteAllInfo(userId, chatId);
        } else {
            return sendMessage(userProcessing.checkInfo(userId) +
                    "\n\n" +
                    deliveryProcessing.checkInfo(userId), chatId);
        }
    }

    @Override
    public Commands getExecuteName() {
        return Commands.ALL_INFO;
    }
}
