package com.example.tg_bot.service.handlers.ordershandler.orderprocessing;

import com.example.tg_bot.entities.Keyboard;
import com.example.tg_bot.entities.Laptop;
import com.example.tg_bot.repo.KeyboardRepository;
import com.example.tg_bot.repo.LaptopRepository;
import com.example.tg_bot.service.handlers.orderpageshandler.OrderPagesHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProcessing {

    private final OrderPagesHandler orderPagesHandler;
    private final LaptopRepository laptopRepository;
    private final KeyboardRepository keyboardRepository;

    public void getOrderInfo(String productId) {
        String category = orderPagesHandler.getCategory();
        if (category.equals("laptop")) {
            Laptop laptop = laptopRepository.findById(Long.valueOf(productId)).orElseThrow();
            laptop.setQuantityInStock(laptop.getQuantityInStock() - 1);
            laptopRepository.save(laptop);
        } else {
            Keyboard keyboard = keyboardRepository.findById(Long.valueOf(productId)).orElseThrow();
            keyboard.setQuantityInStock(keyboard.getQuantityInStock() - 1);
            keyboardRepository.save(keyboard);
        }
    }
}
