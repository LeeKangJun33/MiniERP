package com.example.backenderp.controller;

import com.example.backenderp.entity.Users;
import com.example.backenderp.security.JwtTokenProvider;
import com.example.backenderp.service.EmployeeService;
import com.example.backenderp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<Users> signup(@RequestBody Users user) {
        Users saved =userService.saveUser(user);
        return ResponseEntity.ok(saved);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Users users = userService.authenticate(username,password);
        String token = jwtTokenProvider.generateToken(users.getUsername());

        return ResponseEntity.ok(token);
    }
}
