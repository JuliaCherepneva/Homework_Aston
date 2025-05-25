package org.aston.homework_aston_consumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailModel {
    private String recipient;
    private String subject;
    private String body;
}
