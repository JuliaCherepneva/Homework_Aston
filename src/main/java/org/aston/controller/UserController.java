package org.aston.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.model.UserDTO;
import org.aston.model.UserDTO_withID;
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
    public ResponseEntity<UserDTO_withID> createUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO_withID userDTO_withID = userService.create(userDTO);
        URI location = URI.create("/users/" + userDTO_withID.getId());
        return ResponseEntity.created(location).body(userDTO_withID);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO_withID> getUser(@PathVariable Long id) {
        UserDTO_withID userDTO_withID = userService.get(id);
        URI location = URI.create("/users/" + userDTO_withID.getId());
        return ResponseEntity.created(location).body(userDTO_withID);
    }

    @GetMapping
    public ResponseEntity <List<UserDTO_withID>> getUsers(@RequestParam(defaultValue = "10") Long count) {
        List<UserDTO_withID> users = userService.getCount(count);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}") //идемпотентность
    public ResponseEntity<UserDTO_withID> updateUser(@RequestBody UserDTO userDTO,
                                                     @PathVariable Long id) {
        UserDTO_withID userDTO_withID = userService.update(userDTO, id);
        URI location = URI.create("/users/" + userDTO_withID.getId());
        return ResponseEntity.created(location).body(userDTO_withID);
    }
}
