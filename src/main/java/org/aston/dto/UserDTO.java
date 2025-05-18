package org.aston.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDTO extends RepresentationModel<UserDTO> {
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
