package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/fallback/users")
public class FallbackController {
    @GetMapping
    public ResponseEntity<String> fallbackForUserService() {
        log.warn("Fallback сработал для UserService");
        return ResponseEntity.ok("UserService в данный момент недоступен, повторите запрос позже.");
    }
}
