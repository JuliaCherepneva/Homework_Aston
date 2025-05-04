package org.aston.service;

import org.aston.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDto);
    UserDTO getById(Integer id);
    List<UserDTO> getAll();
    UserDTO update(Integer id, UserDTO userDto);
    void delete(Integer id);
}
