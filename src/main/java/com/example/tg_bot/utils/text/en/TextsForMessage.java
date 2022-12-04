package com.example.tg_bot.utils.text.en;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TextsForMessage {

    START("Welcome to this bot!"),

    HELP("""
            /set_my_info - It's command for set information about you for we can defined you
            /set_my_delivery_info - It's command for set information for we can send to you your package
            /check_info_about_me - It's command for you can see information which you get to the bot
            /help - Info about commands
            """),
CHOOSE_LANGUAGE("""
        Please choose the language:
        English or Ukrainian
        
        Будь-ласка виберіть мову:
        Англійська або Українська"""),
    INFO("""
            What you want to do?
            
            You can:
            1. Set your information
            2. Update your information
            3. Check your information"""),
    WITHOUT_INFO("""
            You don't have information about you and address delivery!
            Please set your information for continue."""),
    ORDERS_INFO("""
            OrderHandler TODO"""),

    SET_OR_UPDATE_USER_INFO("""
            Now you are need send your info:
            1. Your name
            2. Your surname
            3. Your patronymic
            4. Your age
            5. Your mail"""),

    SET_OR_UPDATE_DELIVERY_INFO("""
            Now you are need send info about address where you want get your package:
            1. Your country(optional)
            2. Your region
            3. Your city
            4. Your street
            5. Your post-code
            6. Company to deliver the package
            7. Post office number
            8. Your phone number"""),
    MENU("Now you in menu. Please, choose action"),

    ERROR_DELIVERY_INFO("Sorry, but before you set your delivery information, you will be set information about you."),
    ERROR_UPDATE_DELIVERY_INFO_BY_USER("Sorry, but before you update your delivery information, you will be set information about you."),
    ERROR_UPDATE_DELIVERY_INFO_BY_ADDRESS("Sorry, but before you update your delivery information, you will be set information about your first address."),

    ERROR_USER_INFO("Sorry, but you already register in this bot. Now you can update your info."),
    ERROR_UPDATE_USER_INFO("Sorry, but you don't have any information for update."),
    ERROR_DELIVERY_CHECK_INFO("You don't have information about your delivery address."),
    INCORRECT_COMMAND("Sorry, but we it incorrect command."),
    INCORRECT_AGE("You didn't write a number."),
    NO_IN_STOCK("This item is absent in the stock."),
    DELETED_INFO("All your information has been deleted!"),
    UNKNOWN_ERROR("Unknown error!");

    private final String text;
}
