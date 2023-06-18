package com.projet_gl.rhymni.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import com.projet_gl.rhymni.entity.User;
import com.projet_gl.rhymni.security.AuthRequest;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private StudentService studentService;

    @AfterEach
    void tearDown() {
        reset(studentService, authenticationManager, jwtTokenUtil);
    }

    @Test
    void login_shouldReturnAccessToken() throws Exception {
        // Mock authentication result
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Mock user details
        User user = new User();
        user.setMailAddress("test@example.com");

        // Mock JWT token generation
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenUtil.generateAccessToken(user)).thenReturn("mockAccessToken");

        // Prepare login request
        AuthRequest request = new AuthRequest();
        request.setLogin("test@example.com");
        request.setPassword("password");

        // Perform login request
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"login\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mockAccessToken"));

    }

}
