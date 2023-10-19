package com.example.tgbot.order;

import com.example.tgbot.keyboard.KeyboardRepository;
import com.example.tgbot.laptop.LaptopRepository;
import com.example.tgbot.utils.exceptions.ShopException;
import com.example.tgbot.utils.text.TextSender;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProcessing {

    private static final String INFO_RESPONSE_FORMAT = """
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s""";
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
        var laptop = laptopRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new ShopException("Laptop not found"));
        laptop.setQuantityInStock(laptop.getQuantityInStock() - 1);
        laptopRepository.save(laptop);

        return String.format(INFO_RESPONSE_FORMAT, textSender.getText(userId, "item_id"), laptop.getId(),
                textSender.getText(userId, "item_name"), laptop.getItemName(),
                textSender.getText(userId, "item_price"), laptop.getPrice(),
                textSender.getText(userId, "item_description"), laptop.getDescription(),
                textSender.getText(userId, "item_is_in_stock"), laptop.isInStock());
    }

    private String getKeyboardInfo(Long userId, String productId) {
        var keyboard = keyboardRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new ShopException("Keyboard not found"));
        keyboard.setQuantityInStock(keyboard.getQuantityInStock() - 1);
        keyboardRepository.save(keyboard);

        return String.format(INFO_RESPONSE_FORMAT, textSender.getText(userId, "item_id"), keyboard.getId(),
                textSender.getText(userId, "item_name"), keyboard.getItemName(),
                textSender.getText(userId, "item_price"), keyboard.getPrice(),
                textSender.getText(userId, "item_description"), keyboard.getDescription(),
                textSender.getText(userId, "item_is_in_stock"), keyboard.isInStock());
    }
}
