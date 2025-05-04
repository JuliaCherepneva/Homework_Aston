package org.aston.util;

import org.aston.model.UserDTO;
import org.aston.model.UserDTO_withID;
import org.aston.model.UserModel;

import java.time.LocalDateTime;

public class Mapper {

    public static UserModel toUserModel(UserDTO userDTO) {
        return UserModel.builder().
                name(userDTO.getName())
                .email(userDTO.getEmail())
                .age(userDTO.getAge())
                .createdAt(LocalDateTime.now()).build();
    }

    public static UserDTO_withID toUserModelWithId(UserModel userModel) {
        return UserDTO_withID.builder().
                id(userModel.getId()).
                name(userModel.getName()).
                email(userModel.getEmail()).
                age(userModel.getAge()).
                createdAt(userModel.getCreatedAt()).build();
    }
}
