package com.example.tgbot.utils.exceptions;

public class ShopException extends RuntimeException {
    public ShopException(String message, Exception ex) {
        super(message, ex);
    }

    public ShopException(Exception ex) {
        super(ex);
    }
}
