package com.example.peep.controller;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("login - 로그인 성공")
    void givenCorrectInfo_whenRequestLogin_thenSuccess() throws Exception{
        String userId = "jihoon";
        String password = "1234";

        LoginRequest loginRequest = LoginRequest.of(userId, password);
        JwtToken jwtToken = JwtToken.of("Bearer", "access", "refresh", "jihoon");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Device-Id","UUID")
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value("jihoon"));
    }

    @Test
    @DisplayName("login - 존재하지 않는 아이디로 로그인 요청시 실패")
    void givenNotFoundedId_whenRequestLogin_thenUserNotFound() throws Exception {

        String userId = "jihoon2";
        String password = "1234";

        LoginRequest loginRequest = LoginRequest.of(userId, password);


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Device-Id", "UUID")
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("login - 잘못된 비밀번호로 로그인 요청시 실패 ")
    void givenWrongPassword_whenRequestLogin_thenInvalidPassword() throws Exception{

        String userId = "jihoon";
        String password = "12345";

        LoginRequest loginRequest = LoginRequest.of(userId, password);


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Device-Id","UUID")
                        .content(objectMapper.writeValueAsBytes(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


}