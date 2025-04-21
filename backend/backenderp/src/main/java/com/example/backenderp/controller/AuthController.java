package com.example.backenderp.controller;

import com.example.backenderp.entity.Users;
import com.example.backenderp.security.JwtTokenProvider;
import com.example.backenderp.service.EmployeeService;
import com.example.backenderp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Users users = userService.authenticate(username,password);
        String accessToken = jwtTokenProvider.generateAccessToken(username);
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);
        String token = jwtTokenProvider.generateToken(users.getUsername());

        users.setRefreshToken(refreshToken);
        userService.saveUser(users);

        Map<String,String> tokens  = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String,String>> refresh(@RequestBody Map<String,String> request) {
        String refreshToken = request.get("refresh_token");
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtTokenProvider.getUsername(refreshToken);
        Users users = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을수 없습니다."));

        if (!refreshToken.equals(users.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);

        Map<String,String> response  = new HashMap<>();
        response.put("access_token", newAccessToken);
        return ResponseEntity.ok(response);

    }

}
