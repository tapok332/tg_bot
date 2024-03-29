package com.example.tgbot.bot;

import com.example.tgbot.utils.exceptions.ShopException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;
    private final BotService botService;

    @SneakyThrows
    public Bot(TokenProvider tokenProvider, BotService botService) {
        super(tokenProvider.getBotToken());
        this.botService = botService;

        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/choose_language", "команда для вибору мови бота - bot language selection command"));
        botCommandList.add(new BotCommand("/buy", "для придбання товару - buy items"));

        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException ex) {
            throw new ShopException("Unhappy execution of execute method", ex);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        execute(botService.handleUpdate(update));
    }
}
