package org.aston.service;

import lombok.RequiredArgsConstructor;
import org.aston.dto.UserDTO;
import org.aston.mapper.UserMapper;
import org.aston.model.UserModel;
import org.aston.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserDTO userDto) {
        UserModel user = UserMapper.toEntity(userDto);
        user.setCreatedAt(LocalDateTime.now());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO getById(Integer id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO update(Integer id, UserDTO dto) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
