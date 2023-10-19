package com.example.tgbot.delivery;

import com.example.tgbot.info.BasicInfoHandler;
import com.example.tgbot.user.UserRepository;
import com.example.tgbot.utils.cache.UserData;
import com.example.tgbot.utils.commands.Commands;
import com.example.tgbot.utils.exceptions.ShopException;
import com.example.tgbot.utils.text.TextSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import static com.example.tgbot.utils.sendmessage.Sending.sendMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryProcessing implements BasicInfoHandler {

    private static final String INFO_RESPONSE_FORMAT = """
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s
            %s %s""";
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final UserData userData;
    private final TextSender textSender;

    @Override
    public SendMessage saveInfo(Message message) {
        Long userId = message.getFrom().getId();
        Long chatId = message.getChatId();
        var messageText = message.getText();
        var delivery = deliveryRepository.findByUserId(userId).orElseGet(() -> getNewDelivery(userId));

        return switch (delivery.getState()) {
            case START_SET -> setCountry(messageText, userId, chatId, delivery);
            case SET_COUNTRY -> setRegion(messageText, userId, chatId, delivery);
            case SET_REGION -> setCity(messageText, userId, chatId, delivery);
            case SET_CITY -> setStreet(messageText, userId, chatId, delivery);
            case SET_STREET -> setPostCode(messageText, userId, chatId, delivery);
            case SET_POSTCODE -> setDeliveryCompany(messageText, userId, chatId, delivery);
            case SET_DELIVERY_COMPANY -> setPostalOffice(messageText, userId, chatId, delivery);
            case SET_POSTAL_OFFICE -> setEmail(messageText, userId, chatId, delivery);
            case SET_MAIL -> setPhone(messageText, userId, chatId, delivery);
            default -> sendMessage(textSender.getText(userId, "error_unknown"), chatId);
        };
    }

    @Override
    public String checkInfo(Long userId) {
        var user = userRepository.findByTgUserId(userId);
        if (user.isPresent()) {
            var delivery = deliveryRepository.findByUserId(user.get().getId());
            if (delivery.isPresent()) {
                return String.format(INFO_RESPONSE_FORMAT, textSender.getText(userId, "country_info"), delivery.get().getCountry(),
                        textSender.getText(userId, "region_info"), delivery.get().getRegion(),
                        textSender.getText(userId, "city_info"), delivery.get().getCity(),
                        textSender.getText(userId, "street_info"), delivery.get().getStreet(),
                        textSender.getText(userId, "post_code_info"), delivery.get().getPostCode(),
                        textSender.getText(userId, "delivery_company_info"), delivery.get().getDeliveryCompany(),
                        textSender.getText(userId, "post_office_info"), delivery.get().getPostalOffice(),
                        textSender.getText(userId, "mail_info"), delivery.get().getMail(),
                        textSender.getText(userId, "phone_number_info"), delivery.get().getPhoneNumber());
            }
        }
        userData.saveUsersCurrentBotState(userId, Commands.INFO);

        return textSender.getText(userId, "error_address_check_info");
    }

    @Transactional
    public SendMessage deleteAllInfo(Long userId, Long chatId) {
        var user = userRepository.findByTgUserId(userId);
        if (user.isEmpty()) {
            return sendMessage(textSender.getText(userId, "error_user_check_info"), chatId);
        }

        if (user.get().getAddress() != null) {
            deliveryRepository.deleteById(user.get().getAddress().getId());
        }
        userRepository.deleteByTgUserId(userId);
        userData.saveUsersCurrentBotState(userId, Commands.INFO);
        log.debug("User with id {} was deleted", userId);

        return sendMessage(textSender.getText(userId, "deleted_info"), chatId);
    }

    private SendMessage setCountry(String country, Long userId, Long chatId, Delivery delivery) {
        delivery.setCountry(country);
        log.debug("User with id {} execute set country in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_region"), chatId);
    }

    private SendMessage setRegion(String region, Long userId, Long chatId, Delivery delivery) {
        delivery.setRegion(region);
        log.debug("User with id {} execute set region in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_city"), chatId);
    }

    private SendMessage setCity(String city, Long userId, Long chatId, Delivery delivery) {
        delivery.setCity(city);
        log.info("User with id {} execute set city in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_street"), chatId);
    }

    private SendMessage setStreet(String street, Long userId, Long chatId, Delivery delivery) {
        delivery.setStreet(street);
        log.debug("User with id {} execute set street in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_post_code"), chatId);
    }

    private SendMessage setPostCode(String postcode, Long userId, Long chatId, Delivery delivery) {
        delivery.setPostCode(postcode);
        log.debug("User with id {} execute set post code in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_delivery_company"), chatId);
    }

    private SendMessage setDeliveryCompany(String deliveryCompany, Long userId, Long chatId, Delivery delivery) {
        delivery.setDeliveryCompany(deliveryCompany);
        log.debug("User with id {} execute set delivery company in chat {}", userId, chatId);
        deliveryRepository.save(delivery);

        return sendMessage(textSender.getText(userId, "send_post_office"), chatId);
    }

    private SendMessage setPostalOffice(String postalOffice, Long userId, Long chatId, Delivery delivery) {
        try {
            delivery.setPostalOffice(Integer.parseInt(postalOffice));
        } catch (NumberFormatException ex) {
            log.debug("User with id {} failed execute set postal office in chat {}", userId, chatId);
            return sendMessage(textSender.getText(userId, "error_post_office"), chatId);
        }
        log.debug("User with id {} execute set postal office in chat {}", userId, chatId);
        deliveryRepository.save(delivery);
        return sendMessage(textSender.getText(userId, "send_mail"), chatId);
    }

    private SendMessage setEmail(String email, Long userId, Long chatId, Delivery delivery) {
        if (isValidEmail(email)) {
            delivery.setMail(email);
            log.debug("User with id {} execute set e-mail in chat {}", userId, chatId);
            deliveryRepository.save(delivery);

            return sendMessage(textSender.getText(userId, "send_phone_number"), chatId);
        } else {
            log.debug("User with id {} failed execute set e-mail in chat {}", userId, chatId);
            return sendMessage(textSender.getText(userId, "error_mail"), chatId);
        }
    }

    private SendMessage setPhone(String phone, Long userId, Long chatId, Delivery delivery) {
        if (isValidPhone(phone)) {
            delivery.setPhoneNumber(phone);
            log.debug("User with id {} execute set phone number in chat {}", userId, chatId);
            userData.saveUsersCurrentBotState(userId, Commands.INFO);

            return sendMessage(textSender.getText(userId, "thanks_message"), chatId);
        } else {
            log.debug("User with id {} failed execute set phone number in chat {}", userId, chatId);
            return sendMessage(textSender.getText(userId, "error_phone_num"), chatId);
        }
    }

    private static boolean isValidPhone(String phone) {
        return phone.matches("^[+][0-9]{3}[0-9]{9}$");
    }

    private static boolean isValidEmail(String email) {
        return email.matches("^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private Delivery getNewDelivery(Long userId) {
        var user = userRepository.findByTgUserId(userId).orElseThrow(() -> new ShopException("User not found"));
        return deliveryRepository.save(new Delivery(user));
    }
}
