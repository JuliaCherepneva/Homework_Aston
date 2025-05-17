package org.aston.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.assembler.UserModelAssembler;
import org.aston.dto.UserDTO;
import org.aston.service.UserServiceImpl;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userService;
    private final UserModelAssembler assembler;

    @Operation(summary = "Создание пользователя")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO userDTOResponse = userService.create(userDTO);
        return ResponseEntity
                .created(URI.create("/users/" + userDTOResponse.getId()))
                .body(assembler.toModel(userDTOResponse));
    }

    @Operation(summary = "Получение пользователя")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        UserDTO userDTOResponse = userService.getById(id);
        return ResponseEntity.ok(assembler.toModel(userDTOResponse));
    }

    @Operation(summary = "Получение всех пользователей")
    @GetMapping("/getAll")
    public ResponseEntity<CollectionModel<UserDTO>> getAll() {
        List<UserDTO> users = userService.getAll();
        List<UserDTO> userDTOResponse = users.stream()
                .map(assembler::toModel)
                .toList();
        return ResponseEntity.ok(CollectionModel.of(userDTOResponse));
    }

    @Operation(summary = "Обновление информации о пользователе")
    @PutMapping("update/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO,
                                              @PathVariable("id") Integer id) {
        UserDTO userDTOResponse = userService.update(id, userDTO);
        return ResponseEntity.ok(assembler.toModel(userDTOResponse));
    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
