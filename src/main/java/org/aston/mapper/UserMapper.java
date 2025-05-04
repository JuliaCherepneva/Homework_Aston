package org.aston.mapper;


import org.aston.model.UserModel;
import org.aston.dto.UserDTO;

public class UserMapper {

    public static UserDTO toDto(UserModel model) {
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setEmail(model.getEmail());
        dto.setAge(model.getAge());
        return dto;
    }

    public static UserModel toEntity(UserDTO dto) {
        return UserModel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .age(dto.getAge())
                .build();
    }
}

