package com.example.tgbot.info;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public interface BasicInfoHandler {

    SendMessage saveInfo(Message message);
    SendMessage updateInfo(Message message);
    String checkInfo(Long id);
}
