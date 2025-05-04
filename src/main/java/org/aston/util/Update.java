package org.aston.util;

import org.aston.model.UserDTO;
import org.aston.model.UserModel;

import java.util.Optional;

public class Update {
    public static UserModel toUpdate(UserModel userModel, UserDTO userDTO) {
        Optional.ofNullable(userDTO.getName()).ifPresent(userModel::setName);
        Optional.ofNullable(userDTO.getEmail()).ifPresent(userModel::setEmail);
        Optional.ofNullable(userDTO.getAge()).ifPresent(userModel::setAge);
        Optional.ofNullable(userDTO.getCreatedAt()).ifPresent(userModel::setCreatedAt);
        return userModel;
    }
}
