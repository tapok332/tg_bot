package com.example.tgbot.delivery;

import com.example.tgbot.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    private String region;
    private String city;
    private String street;
    private String postCode;
    private String deliveryCompany;
    private Integer postalOffice;
    private String mail;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private DeliveryProcessingState state;

    @OneToOne
    private User user;

    public Delivery(User user) {
        this.user = user;
        state = DeliveryProcessingState.START_SET;
    }

    public boolean isValid() {
        return country != null && region != null && city != null && postCode != null
                && deliveryCompany != null && phoneNumber != null && mail != null;
    }

    public void setCountry(String country) {
        this.country = country;
        this.state = DeliveryProcessingState.SET_COUNTRY;

    }

    public void setRegion(String region) {
        this.region = region;
        this.state = DeliveryProcessingState.SET_REGION;
    }

    public void setCity(String city) {
        this.city = city;
        this.state = DeliveryProcessingState.SET_CITY;
    }

    public void setStreet(String street) {
        this.street = street;
        this.state = DeliveryProcessingState.SET_STREET;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
        this.state = DeliveryProcessingState.SET_POSTCODE;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
        this.state = DeliveryProcessingState.SET_DELIVERY_COMPANY;
    }

    public void setPostalOffice(Integer postalOffice) {
        this.postalOffice = postalOffice;
        this.state = DeliveryProcessingState.SET_POSTAL_OFFICE;
    }

    public void setMail(String mail) {
        this.mail = mail;
        this.state = DeliveryProcessingState.SET_MAIL;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.state = DeliveryProcessingState.SET_PHONE;
    }
}
