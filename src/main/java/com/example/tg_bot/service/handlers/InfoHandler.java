package com.example.tg_bot.service.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public interface InfoHandler {

    SendMessage saveInfo(Message message);
    SendMessage updateInfo(Message message);
    String checkInfo(Long id);
}
