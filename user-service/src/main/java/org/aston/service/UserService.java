package org.aston.service;

import org.aston.dto.UserDTO;
import java.util.List;
/**
 * Интерфейс для управления сущностями UserModel.
 * Отвечает за CRUD-операции (создание, чтение, обновление, удаление) пользователей в базе данных.
 */
public interface UserService {
    UserDTO create(UserDTO userDto);
    UserDTO getById(Integer id);
    List<UserDTO> getAll();
    UserDTO update(Integer id, UserDTO userDto);
    void delete(Integer id);
}
