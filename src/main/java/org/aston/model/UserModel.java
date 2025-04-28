package org.aston.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {
    // Здесь добавил @Builder для фабрики (такой подход рил показался интересным - паттерны,все дела)
    // и @AllArgsConstructor необходимый для @Builder. Ну просто мы реально очень неплохо разгружаем тесты, наверно.
    // Нужно у нашего лектора спросить.
    // Остальные аннотации сугубо для валидации

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Имя пользователя должно содержать от 3 до 100 символов")
    @Size(min = 3, max = 100)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    @Email
    @NotBlank(message = "Строка не соответствует формату Email и не содержит символа @")
    private String email;

    @Column(name = "age")
    @NotNull(message = "Возраст обязателен для заполнения")
    @Min(value = 8, message = "Возраст пользователя не должен быть меньше 8")
    @Max(value = 99, message = "Возраст пользователя не должен превышать 99 лет")
    private int age;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public UserModel(String name, String email, int age, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

}

