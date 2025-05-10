package org.aston.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.dto.OperationType;
import org.aston.dto.UserDTO;
import org.aston.dto.UserEvent;
import org.aston.exception.EntityNotFoundException;
import org.aston.mapper.UserMapper;
import org.aston.model.UserModel;
import org.aston.repository.UserRepository;
import org.aston.util.Update;
import org.aston.util.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    @Transactional
    @Override
    public UserDTO create(UserDTO userDTO) {
        UserModel userModel =UserMapper.toEntity(userDTO);
        userModel.setCreatedAt(LocalDateTime.now());
        userModel = userRepository.save(userModel);
        log.info("Создан пользователь с ID = {}, имя = {}", userModel.getId(), userModel.getName());
        kafkaTemplate.send("user-events", new UserEvent(userModel.getId(), userModel.getEmail(), OperationType.CREATE));
        return UserMapper.toDtoWithDate(userModel);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO getById(Integer id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        return UserMapper.toDtoWithDate(userModel);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDtoWithDate)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void delete(Integer id) {
        UserModel userModel = userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД"));
        log.info("Пользователь с ID = {} удален", userModel.getId());
        userRepository.deleteById(userModel.getId());
        kafkaTemplate.send("user-events", new UserEvent(userModel.getId(), userModel.getEmail(), OperationType.DELETE));
    }

    @Transactional
    @Override
    public UserDTO update(Integer id, UserDTO userDTO) {
        Validate.validateUser(userDTO);
        Optional<UserModel> userModel = Optional.ofNullable(userRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Пользователь с указанным ID " + id + " не обнаружен в БД")));
        Update.update(userDTO,userModel);
        userModel = Optional.of(userRepository.save(userModel.orElse(null)));
        log.info("Пользователь с ID = {} обновлён", userModel.get().getId());
        return UserMapper.toDtoWithDate(userModel.orElse(null));
    }
}
