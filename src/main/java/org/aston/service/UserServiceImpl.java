package org.aston.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.dto.UserDTO;
import org.aston.exception.EntityNotFoundException;
import org.aston.mapper.UserMapper;
import org.aston.model.UserModel;
import org.aston.repository.UserRepository;
import org.aston.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервисный класс для управления сущностями UserModel.
 * Отвечает за CRUD-операции (создание, чтение, обновление, удаление) пользователей в базе данных.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDTO create(UserDTO userDTO) {
        Optional<UserModel> userModel = Optional.ofNullable(UserMapper.toEntity(userDTO));
        userModel = Optional.of(userRepository.save(userModel.orElse(null)));
        log.info("Создан пользователь с ID = {}, имя = {}", userModel.get().getId(), userModel.get().getName());
        return UserMapper.toDto(userModel.orElse(null));
    }

    @Override
    public UserDTO getById(Integer id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        return UserMapper.toDto(userModel);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Integer id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        log.info("Пользователь с ID = {} удален", userModel.getId());
        userRepository.deleteById(userModel.getId());
    }


    @Override
    public UserDTO update(Integer id, UserDTO userDTO) {
        Validate.validateUser(userDTO);
        Optional<UserModel> userModel = Optional.ofNullable(userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД")));
        userModel.get().setName(userDTO.getName());
        userModel.get().setEmail(userDTO.getEmail());
        userModel.get().setAge(userDTO.getAge());
        userModel.get().setCreatedAt(userDTO.getCreatedAt());
        userModel = Optional.of(userRepository.save(userModel.orElse(null)));
        log.info("Пользователь с ID = {} обновлён", userModel.get().getId());
        return UserMapper.toDto(userModel.orElse(null));
    }
}
