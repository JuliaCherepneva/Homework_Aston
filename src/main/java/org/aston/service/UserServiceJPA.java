package org.aston.service;

import org.aston.model.UserDTO;
import org.aston.model.UserDTO_withID;

import java.util.List;

public interface UserServiceJPA {
    UserDTO_withID create(UserDTO userDTO);
    UserDTO_withID get(Long id);
    List<UserDTO_withID> getCount(Long count);
    UserDTO_withID update(UserDTO userDTO, Long userId);
}
