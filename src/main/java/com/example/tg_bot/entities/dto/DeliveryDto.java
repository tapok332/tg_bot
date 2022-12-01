package com.example.tg_bot.entities.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Data
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryDto {
    private String country;
    private String region;
    private String city;
    private String street;
    private String postCode;
    private String deliveryCompany;
    private String postalOffice;
    private String mail;
    private String phoneNum;
}
