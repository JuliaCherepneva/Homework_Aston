package org.aston.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    private Integer id;
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
