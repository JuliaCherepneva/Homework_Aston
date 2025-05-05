package org.aston.mapper;


import org.aston.model.UserModel;
import org.aston.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDto(UserModel model) {
        return UserDTO.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .age(model.getAge())
                .build();
    } //что бы всё у нас было в одном стиле,раз мы в DTO используем билдер

    public static UserModel toEntity(UserDTO dto) {
        return UserModel.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();
    }
}

