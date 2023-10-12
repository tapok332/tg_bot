package com.example.tg_bot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

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
    private Integer postalOffice;
    @NotNull
    @Column(name = "mail")
    private String mail;
    @NotNull
    @Column(name = "phone_number")
    private String phoneNum;

    @OneToOne(mappedBy = "address")
    private User user;
}
