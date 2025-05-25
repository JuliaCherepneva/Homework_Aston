package org.aston.homework_aston_consumer.mapper;

import  org.aston.homework_aston_consumer.dto.UserEvent;
import  org.aston.homework_aston_consumer.model.EmailModel;

public class EmailMapper {

    public static EmailModel toEmail(UserEvent event) {
        String subject;
        String body;

        switch (event.getOperationType()) {
            case CREATE -> {
                subject = "Добро пожаловать!";
                body = "Пользователь с ID " + event.getUserId() + " был создан.";
            }
            case DELETE -> {
                subject = "Удаление аккаунта";
                body = "Пользователь с ID " + event.getUserId() + " был удалён.";
            }
            default -> {
                subject = "Неизвестное событие";
                body = "Произошло неизвестное событие.";
            }
        }

        return new EmailModel(event.getEmail(), subject, body);
    }
}
