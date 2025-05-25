package org.aston.homework_aston_consumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aston.homework_aston_consumer.dto.UserEvent;
import org.aston.homework_aston_consumer.mapper.EmailMapper;
import org.aston.homework_aston_consumer.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    @Autowired
    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "email-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, UserEvent> record) {
        log.info("Получено сообщение из Kafka: {}", record.value());

        UserEvent userEvent = record.value();

        if (userEvent.getOperationType() != null) {
            int ordinal = userEvent.getOperationType().ordinal();
            log.info("OperationType ordinal: {}", ordinal);
        } else {
            log.warn("OperationType is null for UserEvent: {}", userEvent);
        }

        var email = EmailMapper.toEmail(userEvent);
        emailService.sendEmail(email);
    }
}
