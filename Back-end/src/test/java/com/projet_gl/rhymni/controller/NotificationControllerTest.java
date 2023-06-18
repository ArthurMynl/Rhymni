package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.NotificationResponse;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.service.NotificationService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class NotificationControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private NotificationService notificationService;

        @AfterEach
        void tearDown() {
                reset(notificationService);
        }

        @Test
        @WithMockUser
        void testGetAllNotificationsForTeacher() throws Exception {
                int id = 9;
                List<NotificationResponse> notificationResponses = new ArrayList<>();
                notificationResponses.add(NotificationResponse.builder()
                                .idNotification(1)
                                .personneSend(3)
                                .message("Demande 1")
                                .build());
                notificationResponses.add(NotificationResponse.builder()
                                .idNotification(2)
                                .personneSend(7)
                                .message("Demande 2")
                                .build());

                when(notificationService.getAllNotificationsForUser(id)).thenReturn(notificationResponses);

                mockMvc.perform(get("/notification/getAllNotificationsForUser/" + id))
                                .andExpect(status().isOk())
                                .andExpect(content().json(objectMapper.writeValueAsString(notificationResponses)));

                verify(notificationService, times(1)).getAllNotificationsForUser(id);
        }

        @Test
        @WithMockUser
        void testAddNotification() throws Exception {
                Speciality speciality = new Speciality(1, "MOD");
                NotificationRequest notificationRequest = NotificationRequest.builder()
                                .idNotification(0)
                                .personneSend(1)
                                .speciality(speciality.nameSpeciality)
                                .message("New message")
                                .build();

                doNothing().when(notificationService).addNotification(any(NotificationRequest.class));

                mockMvc.perform(post("/notification/addNotification")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(notificationRequest)))
                                .andExpect(status().isCreated());

                verify(notificationService, times(1)).addNotification(notificationRequest);
        }

        @Test
        @WithMockUser
        void testGetListTeachersForNotification() throws Exception {
                int id = 1;

                Set<Teacher> teachers = new HashSet<>();
                teachers.add(new Teacher(1, "test", "test", null, null, null));
                teachers.add(new Teacher(2, null, null, null, null, null));

                when(notificationService.searchTeachers(id)).thenReturn(teachers);

                // Envoyer la requête GET avec l'ID de test
                mockMvc.perform(MockMvcRequestBuilders
                                .get("/notification/getListTeachersForNotification/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(teachers.size()));

                verify(notificationService, times(1)).searchTeachers(id);
        }

        @Test
        @WithMockUser
        void testGetListStudentsForNotification() throws Exception {
                int id = 1;

                Set<Student> students = new HashSet<>();
                students.add(Student.builder().idUser(1).build());
                students.add(Student.builder().idUser(2).build());

                when(notificationService.searchStudents(id)).thenReturn(students);

                mockMvc.perform(MockMvcRequestBuilders
                                .get("/notification/getListStudentsForNotification/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(students.size()));

                verify(notificationService, times(1)).searchStudents(id);
        }

        @Test
        @WithMockUser
        void testGetAllNotificationsForUser_WhenUserHasNotifications() throws Exception {
                int userId = 1;
                List<NotificationResponse> notifications = Arrays.asList(
                                NotificationResponse.builder()
                                                .idNotification(1)
                                                .personneSend(3)
                                                .message("Demande 1")
                                                .build(),
                                NotificationResponse.builder()
                                                .idNotification(2)
                                                .personneSend(7)
                                                .message("Demande 2")
                                                .build());
                when(notificationService.getAllNotificationsForUser(userId)).thenReturn(notifications);

                mockMvc.perform(get("/notification/getAllNotificationsForUser/{id}", userId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", is(2)))
                                .andExpect(jsonPath("$[0].idNotification", is(1)))
                                .andExpect(jsonPath("$[0].personneSend", is(3)))
                                .andExpect(jsonPath("$[0].message", is("Demande 1")))
                                .andExpect(jsonPath("$[1].idNotification", is(2)))
                                .andExpect(jsonPath("$[1].personneSend", is(7)))
                                .andExpect(jsonPath("$[1].message", is("Demande 2")));

                verify(notificationService, times(1)).getAllNotificationsForUser(userId);
                verifyNoMoreInteractions(notificationService);
        }

        @Test
        @WithMockUser
        void testGetAllNotificationsForUser_WhenUserHasNoNotifications() throws Exception {
                int userId = 1;
                List<NotificationResponse> notifications = Collections.emptyList();
                when(notificationService.getAllNotificationsForUser(userId)).thenReturn(notifications);

                mockMvc.perform(get("/notification/getAllNotificationsForUser/{id}", userId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.length()", is(0)));

                verify(notificationService, times(1)).getAllNotificationsForUser(userId);
                verifyNoMoreInteractions(notificationService);
        }

}