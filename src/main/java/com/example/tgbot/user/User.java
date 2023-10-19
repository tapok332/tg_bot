package com.example.tgbot.user;

import com.example.tgbot.delivery.Delivery;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tgUserId;
    private String name;
    private String surname;
    private String patronymic;
    private Integer age;
    @Enumerated(EnumType.STRING)
    private UserProcessingState state;

    @OneToOne(cascade = CascadeType.ALL)
    private Delivery address;

    public User(Long tgUserId) {
        this.tgUserId = tgUserId;
        this.state = UserProcessingState.START_SET;
    }

    public boolean isValid() {
        return name != null && surname != null;
    }

    public void setName(String name) {
        this.name = name;
        this.state = UserProcessingState.SET_NAME;
    }

    public void setSurname(String surname) {
        this.surname = surname;
        this.state = UserProcessingState.SET_SURNAME;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
        this.state = UserProcessingState.SET_PATRONYMIC;
    }

    public void setAge(Integer age) {
        this.age = age;
        this.state = UserProcessingState.SET_AGE;
    }
}
