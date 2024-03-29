package com.example.tgbot.order;

import com.example.tgbot.keyboard.KeyboardRepository;
import com.example.tgbot.laptop.LaptopRepository;
import com.example.tgbot.utils.text.TextSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;
import static com.example.tgbot.utils.sendmessage.Sending.sendPages;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderPagesHandler {

    private List<?> items;
    @Getter
    private String category;
    private int previous;
    private final LaptopRepository laptopRepository;
    private final KeyboardRepository keyboardRepository;
    private final TextSender textSender;

    public BotApiMethod<? extends Serializable> handlePages(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackQueryData = callbackQuery.getData();
        Long userId = callbackQuery.getFrom().getId();

        getItems(callbackQueryData);
        if (items == null || items.isEmpty()) {
            return sendMessage(textSender.getText(userId, "error_items"), chatId);
        }
        try {
            return sendPages(chatId, messageId, getCurrentButtons(callbackQueryData, userId), getCurrentItem());
        } catch (Exception e) {
            log.info(e.getMessage());
            return sendMessage(textSender.getText(userId, "error_incorrect_command"), chatId);
        }
    }

    private void getItems(String callbackQueryData) {
        if (callbackQueryData.equals("laptops")) {
            category = "laptops";
            items = laptopRepository.findAll();
        }
        if (callbackQueryData.equals("keyboards")) {
            category = "keyboards";
            items = keyboardRepository.findAll();
        }
    }

    public String getCurrentItem() {
        return items.stream()
                .map(Object::toString)
                .toList()
                .get(previous);
    }

    private List<List<InlineKeyboardButton>> getCurrentButtons(String callbackQueryData, Long userId) {
        int pageNumber;
        int next;
        if (callbackQueryData.equals("page 1") || callbackQueryData.equals("laptops") || callbackQueryData.equals("keyboards")) {
            pageNumber = 1;
            previous = 0;
            next = 2;
            return getButtons(userId, List.of(" ", String.valueOf(pageNumber), ">"), List.of(" ", "page " + next));
        }

        pageNumber = Integer.parseInt(callbackQueryData.substring(5));
        previous = pageNumber - 1;
        next = pageNumber + 1;
        if ((pageNumber == 1 && items.size() == 2) || pageNumber == items.size()) {
            return getButtons(userId, List.of("<", String.valueOf(pageNumber), " "),
                    List.of(String.format("page %d", previous), " "));
        }
        if (!callbackQueryData.contains("1")) {
            return getButtons(userId, List.of("<", String.valueOf(pageNumber), ">"),
                    List.of("page " + previous, "page " + next));
        }
        if (items.get(1) == null) {
            return getButtons(userId, List.of(" ", String.valueOf(pageNumber), " "), List.of(" ", " "));
        }
        return null;
    }

    private List<List<InlineKeyboardButton>> getButtons(Long userId, List<String> text, List<String> callBackData) {
        String page = textSender.getText(userId, "page");
        InlineKeyboardButton previousPage = InlineKeyboardButton.builder()
                .text(text.get(0))
                .callbackData(callBackData.get(0))
                .build();
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(page + text.get(1))
                .callbackData(" ")
                .build();
        InlineKeyboardButton nextPage = InlineKeyboardButton.builder()
                .text(text.get(2))
                .callbackData(callBackData.get(1))
                .build();
        List<InlineKeyboardButton> keyboard = List.of(previousPage, countPage, nextPage);

        return List.of(keyboard);
    }
}
