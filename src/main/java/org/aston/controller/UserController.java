package org.aston.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.dto.UserDTO;
import org.aston.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO userDTOResponse = userService.create(userDTO);
        URI location = URI.create("/users/" + userDTOResponse.getId());
        return ResponseEntity.created(location).body(userDTOResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Integer id) {
        UserDTO userDTOResponse = userService.getById(id);
        URI location = URI.create("/users/" + userDTOResponse.getId());
        return ResponseEntity.created(location).body(userDTOResponse);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PutMapping("update/{id}") //идемпотентность
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO,
                                                     @PathVariable("id") Integer id) {
        UserDTO userDTOResponse = userService.update(id, userDTO);
        URI location = URI.create("/users/" + userDTOResponse.getId());
        return ResponseEntity.ok(userDTOResponse);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
