package com.example.tg_bot.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "keyboard_items_info")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Keyboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String itemName;
    private Double price;
    private String description;
    private Integer quantityInStock;

    public boolean isInStock(){
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
