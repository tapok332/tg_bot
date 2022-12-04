package com.example.tg_bot.service.handlers.orderpageshandler;

import com.example.tg_bot.repo.KeyboardRepository;
import com.example.tg_bot.repo.LaptopRepository;
import com.example.tg_bot.service.handlers.languagehandler.LanguageHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.example.tg_bot.utils.utilforsendmessage.Sending.*;

@Component
@RequiredArgsConstructor
public class OrderPagesHandler {

    private List<List<InlineKeyboardButton>> buttons;
    private List<?> items;
    private final LaptopRepository laptopRepository;
    private final KeyboardRepository keyboardRepository;
    private String category;
    private int currentPage;
    private int pageNumber;
    private final LanguageHandler languageHandler;

    public BotApiMethod<? extends Serializable> handlePages(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String callbackQueryData = callbackQuery.getData();
        Long userId = callbackQuery.getFrom().getId();

//        if (callbackQueryData.equals("laptop") || callbackQueryData.equals("keyboard")) {
//            return sendMessageWithButton(getFirstPage(1, callbackQueryData, userId), chatId, buttons);
//        }
        getItems(callbackQueryData, userId);
        if(items == null){
            return sendMessage(languageHandler.getText(userId, "error_items"), chatId);
        }
        return sendPages(chatId, messageId, getPage(callbackQueryData, userId), buttons);
    }

    private void getItems(String callbackQueryData, Long userId) {
        if (callbackQueryData.equals("laptops")) {
            category = "laptops";
            pageNumber = 1;
            buttons = getFirstButtons(pageNumber, userId);
            items = IteratorUtils.toList(laptopRepository.findAll().iterator());
        }
        if (callbackQueryData.equals("keyboards")) {
            category = "keyboards";
            pageNumber = 1;
            buttons = getFirstButtons(pageNumber, userId);
            items = IteratorUtils.toList(keyboardRepository.findAll().iterator());
        }
    }

    public String getCategory() {
        return category;
    }

    public String getCurrentItem() {
        return items.get(currentPage-1).toString();
    }

    private String getPage(String callbackQueryData, Long userId) {
        List<String> item = items.stream()
                .map(Object::toString)
                .toList();

        if (item.size() == pageNumber || callbackQueryData.contains("1")) {
            pageNumber = Integer.parseInt(callbackQueryData.substring(5));
            buttons = getLastButtons(pageNumber, userId);
        }
        if(!callbackQueryData.contains("1") && !callbackQueryData.equals("laptop")
                && callbackQueryData.equals("keyboard")){
            pageNumber = Integer.parseInt(callbackQueryData.substring(5));
            buttons = getButtons(pageNumber, userId);
        }
        if (item.get(1) == null) {
            buttons = getOneButton(pageNumber, userId);
        }

        return item.get(pageNumber - 1);
    }

    private String getFirstPage(int pageNumber, String callbackQueryData, Long userId) {
        if (callbackQueryData.equals("laptops")) {
            category = "laptops";
            items = IteratorUtils.toList(laptopRepository.findAll().iterator());
        }
        if (callbackQueryData.equals("keyboards")) {
            category = "keyboards";
            items = IteratorUtils.toList(keyboardRepository.findAll().iterator());
        }


        List<String> item = items.stream()
                .map(Object::toString)
                .toList();

        if (item.size() == pageNumber) {
            buttons = getOneButton(pageNumber, userId);
        } else {
            buttons = getFirstButtons(pageNumber, userId);
        }

        return item.get(pageNumber - 1);
    }

    private String getSecondPage(int pageNumber, Long userId) {
        List<String> strings = items.stream()
                .map(Object::toString)
                .toList();
        if (strings.size() == pageNumber) {
            buttons = getLastButtons(pageNumber, userId);
        } else {
            buttons = getButtons(pageNumber, userId);
        }

        return strings.get(pageNumber - 1);
    }

    private String getThirdPage(int pageNumber, Long userId) {
        List<String> strings = items.stream()
                .map(Object::toString)
                .toList();
        if (strings.size() == pageNumber) {
            buttons = getLastButtons(pageNumber, userId);
        } else {
            buttons = getButtons(pageNumber, userId);
        }

        return strings.get(pageNumber - 1);
    }

    private List<List<InlineKeyboardButton>> getFirstButtons(int pageNumber, Long userId) {
        currentPage = pageNumber;
        String page = languageHandler.getText(userId, "page");
        InlineKeyboardButton emptyButton = InlineKeyboardButton.builder()
                .text(" ")
                .callbackData(" ")
                .build();
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(page + pageNumber)
                .callbackData(" ")
                .build();
        InlineKeyboardButton firstPage = InlineKeyboardButton.builder()
                .text(">")
                .callbackData("page " + ++pageNumber)
                .build();
        List<InlineKeyboardButton> keyboard = List.of(emptyButton, countPage, firstPage);

        return List.of(keyboard);
    }

    private List<List<InlineKeyboardButton>> getButtons(int pageNumber, Long userId) {
        InlineKeyboardButton previousPage = InlineKeyboardButton.builder()
                .text("<")
                .callbackData("page " + currentPage)
                .build();
        currentPage = pageNumber;
        String page = languageHandler.getText(userId, "page");
        InlineKeyboardButton nextPage = InlineKeyboardButton.builder()
                .text(">")
                .callbackData("page " + ++pageNumber)
                .build();
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(page + pageNumber)
                .callbackData(" ")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(previousPage, countPage, nextPage);

        return List.of(keyboard);
    }

    private List<List<InlineKeyboardButton>> getLastButtons(int pageNumber, Long userId) {
        InlineKeyboardButton previousPage = InlineKeyboardButton.builder()
                .text("<")
                .callbackData("page " + currentPage)
                .build();
        currentPage = pageNumber;
        String page = languageHandler.getText(userId, "page");
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(page + pageNumber)
                .callbackData(" ")
                .build();
        InlineKeyboardButton nextPage = InlineKeyboardButton.builder()
                .text(" ")
                .callbackData(" ")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(previousPage, countPage, nextPage);

        return List.of(keyboard);
    }

    private List<List<InlineKeyboardButton>> getOneButton(int pageNumber, Long userId) {
        InlineKeyboardButton previousPage = InlineKeyboardButton.builder()
                .text(" ")
                .callbackData(" ")
                .build();
        String page = languageHandler.getText(userId, "page");
        InlineKeyboardButton countPage = InlineKeyboardButton.builder()
                .text(page + pageNumber)
                .callbackData(" ")
                .build();
        InlineKeyboardButton nextPage = InlineKeyboardButton.builder()
                .text(" ")
                .callbackData(" ")
                .build();
        List<InlineKeyboardButton> keyboard = List.of(previousPage, countPage, nextPage);

        return List.of(keyboard);
    }
}
