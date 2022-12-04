package com.example.tg_bot.utils.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {
    START,
    USER_INFO,
    DELIVERY_INFO,
    ALL_INFO,
    SET_USER_INFO,
    SET_NAME,
    SET_SURNAME,
    SET_PATRONYMIC,
    SET_AGE,
    UPDATE_USER_INFO,
    UPDATE_NAME,
    UPDATE_SURNAME,
    UPDATE_PATRONYMIC,
    UPDATE_AGE,
    SET_DELIVERY_INFO,
    SET_COUNTRY,
    SET_REGION,
    SET_CITY,
    SET_STREET,
    SET_POST_CODE,
    SET_DELIVERY_COMPANY,
    SET_POSTAL_OFFICE,
    SET_MAIL,
    SET_PHONE,
    UPDATE_DELIVERY_INFO,
    UPDATE_COUNTRY,
    UPDATE_REGION,
    UPDATE_CITY,
    UPDATE_STREET,
    UPDATE_POST_CODE,
    UPDATE_DELIVERY_COMPANY,
    UPDATE_POSTAL_OFFICE,
    UPDATE_MAIL,
    UPDATE_PHONE,
    CHECK_USER_INFO,
    KEYBOARD_ORDERS,
    LAPTOP_ORDERS,
    HELP,
    INFO,
    BUY,
    TEXT_PROCESSING,
    MENU,
    CHECK_ALL_INFO, USER_AND_ORDER_INFO, DELETE_USER_INFO, NONE
}
