package com.example.tg_bot.service.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface DefaultHandler {
    SendMessage handle();
}
