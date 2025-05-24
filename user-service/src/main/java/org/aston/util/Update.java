package org.aston.util;

import org.aston.dto.UserDTO;
import org.aston.model.UserModel;

import java.util.Optional;

public class Update {
    public static void update(UserDTO userDTO, Optional<UserModel> userModel) {
        userModel.ifPresent(target -> {
            Optional.ofNullable(userDTO.getName()).ifPresent(target::setName);
            Optional.ofNullable(userDTO.getEmail()).ifPresent(target::setEmail);
            Optional.ofNullable(userDTO.getAge()).ifPresent(target::setAge);
            Optional.ofNullable(userDTO.getCreatedAt()).ifPresent(target::setCreatedAt);
        });
    }
}
