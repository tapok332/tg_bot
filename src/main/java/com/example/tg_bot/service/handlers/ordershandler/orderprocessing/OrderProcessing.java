package com.example.tg_bot.service.handlers.ordershandler.orderprocessing;

import com.example.tg_bot.entities.Keyboard;
import com.example.tg_bot.entities.Laptop;
import com.example.tg_bot.repo.KeyboardRepository;
import com.example.tg_bot.repo.LaptopRepository;
import com.example.tg_bot.utils.text.TextSender;
import com.example.tg_bot.service.handlers.orderpageshandler.OrderPagesHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProcessing {

    private final OrderPagesHandler orderPagesHandler;
    private final LaptopRepository laptopRepository;
    private final KeyboardRepository keyboardRepository;
    private final TextSender textSender;

    public String getOrderInfo(@NonNull Long userId, String productId) {
        String category = orderPagesHandler.getCategory();
        if (category.equals("laptops")) {
            return getLaptopInfo(userId, productId);
        } else {
            return getKeyboardInfo(userId, productId);
        }
    }

    private String getLaptopInfo(Long userId, String productId) {
        Laptop laptop = laptopRepository.findById(Long.valueOf(productId)).orElseThrow();
        laptop.setQuantityInStock(laptop.getQuantityInStock() - 1);
        laptopRepository.save(laptop);
        return textSender.getText(userId, "item_id") + laptop.getId()
                + "\n" + textSender.getText(userId, "item_name") + laptop.getItemName()
                + "\n" + textSender.getText(userId, "item_price") + laptop.getPrice()
                + "\n" + textSender.getText(userId, "item_description") + laptop.getDescription()
                + "\n" + textSender.getText(userId, "item_is_in_stock") + laptop.isInStock();
    }

    private String getKeyboardInfo(Long userId, String productId) {
        Keyboard keyboard = keyboardRepository.findById(Long.valueOf(productId)).orElseThrow();
        keyboard.setQuantityInStock(keyboard.getQuantityInStock() - 1);
        keyboardRepository.save(keyboard);
        return textSender.getText(userId, "item_id") + keyboard.getId()
                + "\n" + textSender.getText(userId, "item_name") + keyboard.getItemName()
                + "\n" + textSender.getText(userId, "item_price") + keyboard.getPrice()
                + "\n" + textSender.getText(userId, "item_description") + keyboard.getDescription()
                + "\n" + textSender.getText(userId, "item_is_in_stock") + keyboard.isInStock();
    }
}
