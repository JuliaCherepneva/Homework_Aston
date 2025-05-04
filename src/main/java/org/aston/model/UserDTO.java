package org.aston.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Data
@Builder
public class UserDTO {
    @NotBlank(message = "Имя пользователя должно содержать от 3 до 100 символов")
    @Size(min = 3, max = 100)
    private String name;
    @Email
    @NotBlank(message = "Строка не соответствует формату Email и не содержит символа @")
    private String email;
    @NotNull(message = "Возраст обязателен для заполнения")
    @Min(value = 8, message = "Возраст пользователя не должен быть меньше 8")
    @Max(value = 99, message = "Возраст пользователя не должен превышать 99 лет")
    private int age;
    private LocalDateTime createdAt;
}
