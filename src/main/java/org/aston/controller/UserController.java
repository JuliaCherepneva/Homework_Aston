package org.aston.controller;

import lombok.RequiredArgsConstructor;
import org.aston.dto.UserDTO;
import org.aston.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
}
