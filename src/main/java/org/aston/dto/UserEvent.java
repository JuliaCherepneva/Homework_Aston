package org.aston.dto;

import lombok.Data;

@Data
public class UserEvent {
    private int userId;
    private String email;
    private OperationType operation;

    public UserEvent(int id, String email, OperationType operationType) {
    }
}
