package org.aston.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class UserDTO_withID {
    private int id;
    private String name;
    private String email;
    private int age;
    private LocalDateTime createdAt;
}
