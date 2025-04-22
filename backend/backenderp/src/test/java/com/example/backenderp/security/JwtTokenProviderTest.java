package com.example.backenderp.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

    @Test
    @DisplayName("AccessToken 생성 및 파싱 성공")
   void accessToken_생성_테스트(){
        String username = "testuser";
        String token = jwtTokenProvider.generateAccessToken(username);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(username, jwtTokenProvider.getUsername(token));
   }

    @Test
    @DisplayName("RefreshToken 생성 및 username 파싱 성공")
    void refreshToken_생성_테스트() {
        String username = "testuser";
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);

        assertNotNull(refreshToken);
        assertTrue(jwtTokenProvider.validateToken(refreshToken));
        assertEquals(username, jwtTokenProvider.getUsername(refreshToken));
    }


}