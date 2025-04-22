package com.example.backenderp.service;

import com.example.backenderp.entity.Users;
import com.example.backenderp.repository.UserRepository;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    private UserRepository  userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입시 비밀번호가 암호화되어 저장됨")
    void 회원가입_비밀번호_암호화(){

        Users user = Users.builder()
                .username("testuser")
                .password("1234")
                .email("t@test.com")
                .build();

        when(userRepository.existsByUsername("tester")).thenReturn(false);
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users savedUser = userService.registerUser(user);

        assertNotEquals("1234",savedUser.getPassword());
        assertTrue(passwordEncoder.matches("1234", savedUser.getPassword()));
    }

    @Test
    @DisplayName("로그인 성공시 사용자 정보 반환")
    void 로그인_성공(){
        String rawPassword = "pass";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Users mockUser = Users.builder()
                .username("tester")
                .password(encodedPassword)
                .build();
        when(userRepository.findByUsername("tester")).thenReturn(Optional.of(mockUser));

        Users user = userService.authenticate("tester",rawPassword);
        assertEquals("tester", user.getUsername());

    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 시 예외 발생")
    void 로그인_실패_예외() {
        when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.authenticate("nope", "pass"));

        assertEquals("사용자가 존재하지 않습니다.", ex.getMessage());
    }
}