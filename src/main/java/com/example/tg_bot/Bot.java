package com.example.tg_bot;

import com.example.tg_bot.service.services.BotService;
import com.example.tg_bot.utils.config.BotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final BotService botService;

    @SneakyThrows
    public Bot(BotConfig config, BotService botService) {
        this.config = config;
        this.botService = botService;

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/choose_language", "команда для вибору мови бота - bot language selection command"));
        botCommandList.add(new BotCommand("/buy", "для придбання товару - buy items"));

        this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        execute(botService.handleUpdate(update));
    }
}
