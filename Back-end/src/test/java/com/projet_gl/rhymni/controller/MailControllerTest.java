package com.projet_gl.rhymni.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.projet_gl.rhymni.dto.MailRequest;
import com.projet_gl.rhymni.service.MailService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class MailControllerTest {
    
    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        reset(mailService);
    }


    @Test
    @WithMockUser
    void testSendMailForTeacher() throws Exception {
        LocalDateTime date = LocalDateTime.of(2023, 5, 14, 12, 0);
        MailRequest mailRequest = MailRequest.builder()
                .to("teacher@exemple.com")
                .idTeam(1)
                .date(date)
                .build();
    
        mockMvc.perform(post("/mail/sendTeacher"))
                .andExpect(jsonPath("$[0].date").value("2023-05-14T12:00:00"))
                .andExpect(status().isCreated());
        verify(mailService, times(1)).sendMailForTeacher(mailRequest.getTo(), mailRequest.getIdTeam(), mailRequest.getDate());
    }

}
