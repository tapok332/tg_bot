package com.example.tg_bot.entities;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Component
@Table(name = "delivery_info")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "country")
    private String country;
    @NotNull
    @Column(name = "region")
    private String region;
    @NotNull
    @Column(name = "city")
    private String city;
    @NotNull
    @Column(name = "street")
    private String street;
    @NotNull
    @Column(name = "post_code")
    private String postCode;
    @NotNull
    @Column(name = "delivery_company")
    private String deliveryCompany;
    @NotNull
    @Column(name = "postal_office")
    private String postalOffice;
    @NotNull
    @Column(name = "mail")
    private String mail;
    @NotNull
    @Column(name = "phone_number")
    private String phoneNum;

    @OneToOne(mappedBy = "address")
    private User user;

    @Override
    public String toString() {
        return "Country = " + country + ",\n" +
                "Region = " + region + ",\n" +
                "City = " + city + ",\n" +
                "Street = " + street + ",\n" +
                "PostCode = " + postCode + ",\n" +
                "DeliveryCompany = " + deliveryCompany + ",\n" +
                "PostalOffice = " + postalOffice + ",\n" +
                "PhoneNum = " + phoneNum;
    }
}
