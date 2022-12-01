package com.example.tg_bot.entities;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @Override
    public String toString() {
        if(address != null){
            return "Your name = " + name + ",\n" +
                    "Your surname = " + surname + ",\n" +
                    "Your patronymic = " + patronymic + ",\n" +
                    "Your age = " + age + ",\n" +
                    "Your address: ";
        }
        return "Your name = " + name + ",\n" +
                "Your surname = " + surname + ",\n" +
                "Your patronymic = " + patronymic + ",\n" +
                "Your age = " + age;
    }
}
