package org.aston.homework_aston_consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.aston.homework_aston_consumer.model.EmailModel;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendEmail(EmailModel email) {
        log.info("Отправка email на адрес: {}", email.getRecipient());
        log.info("Тема: {}", email.getSubject());
        log.info("Сообщение: {}", email.getBody());
    }
}
