package com.example.tg_bot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "users_info")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @NotNull
    @Column(name = "user_id")
    private Long userId;
    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "surname")
    private String surname;
    @NotNull
    @Column(name = "patronymic")
    private String patronymic;
    @NotNull
    @Column(name = "age")
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Delivery address;
}
