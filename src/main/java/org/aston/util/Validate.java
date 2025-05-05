package org.aston.util;

import jakarta.validation.*;
import org.aston.dto.UserDTO;
import org.aston.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Утилитарный класс для валидации данных пользователя.
 */
public class Validate {
    private static final Logger logger = LoggerFactory.getLogger(Validate.class);

    public static void validateUser(UserDTO userDTO) {
        if (userDTO.getName().isBlank()) {
            throw new ValidationException("Имя пользователя не может быть пустым");
        }
        if (userDTO.getName().length() < 3 || userDTO.getName().length() > 100) {
            throw new ValidationException("Имя пользователя должно содержать от 3 до 100 символов");
        }
        if (!userDTO.getEmail().contains("@")) {
            throw new ValidationException("Строка не соответствует формату Email и не содержит символа @");
        }
        if (userDTO.getAge() < 8 || userDTO.getAge() > 99) {
            throw new ValidationException("Возраст пользователя не должен быть меньше 8 и больше 99 лет");
        }
        if (userDTO.getCreatedAt().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Дата создания пользователя не может быть в будущем");
        }
    }
}

