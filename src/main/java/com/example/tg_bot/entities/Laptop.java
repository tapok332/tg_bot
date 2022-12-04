package com.example.tg_bot.entities;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Component
@Table(name = "laptop_items_info")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Laptop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String itemName;
    private Double price;
    private String description;
    private Integer quantityInStock;

    public boolean isInStock() {
        return quantityInStock != 0;
    }

    @Override
    public String toString() {
        return "Product ID: " + id
                + "\nName: " + itemName
                + "\nPrice: " + price
                + "\nDescription: " + description
                + "\nIn stock: " + isInStock();
    }
}
