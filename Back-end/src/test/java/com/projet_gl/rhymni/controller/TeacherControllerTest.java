package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.PlanningRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.ReviewRequest;
import com.projet_gl.rhymni.dto.SpecialityRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.dto.TeacherRequest;
import com.projet_gl.rhymni.dto.TeacherResponse;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.NotificationService;
import com.projet_gl.rhymni.service.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class TeacherControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        JwtTokenUtil jwtTokenUtil;

        @MockBean
        private TeacherService teacherService;

        @MockBean
        private NotificationService notificationService;

        @AfterEach
        void tearDown() {
                reset(teacherService, notificationService);
        }

        @Test
        @WithMockUser
        void testCreatePairOfTeacher() throws Exception {
                TeacherRequest teacherRequest = TeacherRequest.builder()
                                .idTeacher1(1)
                                .idTeacher2(2)
                                .build();

                mockMvc.perform(put("/teacher/pairTeachers")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(teacherRequest)))
                                .andExpect(status().isNoContent());

                verify(teacherService, times(1)).createPairOfTeacher(teacherRequest);
        }

        @Test
        @WithMockUser
        void testAddSpeciality() throws Exception {
                SpecialityRequest specialityRequest = new SpecialityRequest(1, 2);

                mockMvc.perform(post("/teacher/addSpeciality")
                                .contentType("application/json")
                                .header("Authorization", "Bearer "
                                                + "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(objectMapper.writeValueAsString(specialityRequest)))
                                .andExpect(status().isCreated());

                verify(teacherService, times(1)).addSpeciality(specialityRequest);
        }

        @Test
        @WithMockUser
        void testRemoveSpeciality() throws Exception {
                SpecialityRequest specialityRequest = new SpecialityRequest(1, 2);

                mockMvc.perform(post("/teacher/removeSpeciality")
                                .contentType("application/json")
                                .header("Authorization",
                                                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(objectMapper.writeValueAsString(specialityRequest)))
                                .andExpect(status().isOk());

                verify(teacherService, times(1)).removeSpeciality(specialityRequest);
        }

        @Test
        @WithMockUser
        void testGetTeacher() throws Exception {
                TeacherResponse teacherResponse = TeacherResponse.builder()
                                .id(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .role("TEACHER")
                                .teacherLinkUserId(2)
                                .specialities(new HashSet<>())
                                .build();

                when(teacherService.getTeacher(anyInt())).thenReturn(teacherResponse);

                mockMvc.perform(get("/teacher/getTeacher/{id}", 1))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.name").value("Louis"))
                                .andExpect(jsonPath("$.surname").value("Legendre"))
                                .andExpect(jsonPath("$.mailAddress").value("louis.legendre@example.com"))
                                .andExpect(jsonPath("$.role").value("TEACHER"))
                                .andExpect(jsonPath("$.teacherLinkUserId").value(2))
                                .andExpect(jsonPath("$.specialities").isEmpty());

                verify(teacherService, times(1)).getTeacher(1);
        }

        @Test
        @WithMockUser
        void testViewTeacher() throws Exception {
                List<TeacherResponse> teacherResponses = Arrays.asList(
                                TeacherResponse.builder()
                                                .id(1)
                                                .name("Louis")
                                                .surname("Legendre")
                                                .mailAddress("louis.legendre@example.com")
                                                .role("TEACHER")
                                                .teacherLinkUserId(2)
                                                .specialities(new HashSet<>())
                                                .build());

                when(teacherService.getAllTeachers()).thenReturn(teacherResponses);

                mockMvc.perform(get("/teacher/getTeacher"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("Louis"))
                                .andExpect(jsonPath("$[0].surname").value("Legendre"))
                                .andExpect(jsonPath("$[0].mailAddress").value("louis.legendre@example.com"))
                                .andExpect(jsonPath("$[0].role").value("TEACHER"))
                                .andExpect(jsonPath("$[0].teacherLinkUserId").value(2))
                                .andExpect(jsonPath("$[0].specialities").isEmpty());

                verify(teacherService, times(1)).getAllTeachers();
        }

        @Test
        @WithMockUser
        void testViewPairTeacher() throws Exception {
                List<List<TeacherResponse>> linkedTeachers = Arrays.asList(
                                Arrays.asList(
                                                TeacherResponse.builder()
                                                                .id(1)
                                                                .name("Louis")
                                                                .surname("Legendre")
                                                                .mailAddress("louis.legendre@example.com")
                                                                .role("TEACHER")
                                                                .teacherLinkUserId(2)
                                                                .specialities(new HashSet<>())
                                                                .build(),
                                                TeacherResponse.builder()
                                                                .id(2)
                                                                .name("Lou")
                                                                .surname("Legend")
                                                                .mailAddress("lou.legend@example.com")
                                                                .role("TEACHER")
                                                                .teacherLinkUserId(1)
                                                                .specialities(new HashSet<>())
                                                                .build()));

                when(teacherService.getAllPairTeacher()).thenReturn(linkedTeachers);

                mockMvc.perform(get("/teacher/getPairTeacher"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0]", hasSize(2)))
                                .andExpect(jsonPath("$[0][0].id").value(1))
                                .andExpect(jsonPath("$[0][0].name").value("Louis"))
                                .andExpect(jsonPath("$[0][0].surname").value("Legendre"))
                                .andExpect(jsonPath("$[0][0].mailAddress").value("louis.legendre@example.com"))
                                .andExpect(jsonPath("$[0][0].role").value("TEACHER"))
                                .andExpect(jsonPath("$[0][0].teacherLinkUserId").value(2))
                                .andExpect(jsonPath("$[0][0].specialities").isEmpty())
                                .andExpect(jsonPath("$[0][1].id").value(2))
                                .andExpect(jsonPath("$[0][1].name").value("Lou"))
                                .andExpect(jsonPath("$[0][1].surname").value("Legend"))
                                .andExpect(jsonPath("$[0][1].mailAddress").value("lou.legend@example.com"))
                                .andExpect(jsonPath("$[0][1].role").value("TEACHER"))
                                .andExpect(jsonPath("$[0][1].teacherLinkUserId").value(1))
                                .andExpect(jsonPath("$[0][1].specialities").isEmpty());

                verify(teacherService, times(1)).getAllPairTeacher();
        }

        @Test
        @WithMockUser
        void testUpdateReview() throws Exception {
                ReviewRequest reviewRequest = ReviewRequest.builder()
                                .idConsulting(1)
                                .review("New review")
                                .build();

                mockMvc.perform(put("/teacher/updateReview")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(reviewRequest)))
                                .andExpect(status().isNoContent());

                verify(teacherService, times(1)).updateReview(reviewRequest);
        }

        @Test
        @WithMockUser
        void testGetConsultings() throws Exception {
                List<ConsultingResponse> consultingResponse = new ArrayList<>();

                ConsultingResponse consulting1 = ConsultingResponse.builder()
                                .idConsulting(1)
                                .review("Review 1")
                                .idPlanning(1)
                                .idRoom(1)
                                .idTeacher(1)
                                .idTeam(1)
                                .speciality("DEV")
                                .build();
                consultingResponse.add(consulting1);

                ConsultingResponse consulting2 = ConsultingResponse.builder()
                                .idConsulting(2)
                                .review("Review 2")
                                .idPlanning(2)
                                .idRoom(2)
                                .idTeacher(2)
                                .idTeam(2)
                                .speciality("INF")
                                .build();
                consultingResponse.add(consulting2);

                when(teacherService.getConsultings()).thenReturn(consultingResponse);

                mockMvc.perform(get("/teacher/getConsultings"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].idConsulting").value(1))
                                .andExpect(jsonPath("$[0].idRoom").value(1))
                                .andExpect(jsonPath("$[0].idPlanning").value(1))
                                .andExpect(jsonPath("$[0].idTeam").value(1))
                                .andExpect(jsonPath("$[0].idTeacher").value(1))
                                .andExpect(jsonPath("$[0].review").value("Review 1"))
                                .andExpect(jsonPath("$[0].speciality").value("DEV"))
                                .andExpect(jsonPath("$[1].idConsulting").value(2))
                                .andExpect(jsonPath("$[1].idRoom").value(2))
                                .andExpect(jsonPath("$[1].idPlanning").value(2))
                                .andExpect(jsonPath("$[1].idTeam").value(2))
                                .andExpect(jsonPath("$[1].idTeacher").value(2))
                                .andExpect(jsonPath("$[1].review").value("Review 2"))
                                .andExpect(jsonPath("$[1].speciality").value("INF"));

                verify(teacherService, times(1)).getConsultings();
        }

        @Test
        @WithMockUser
        void testGetConsultingById() throws Exception {
                int id = 1;
                ConsultingResponse expectedResponse = ConsultingResponse.builder()
                                .idConsulting(id)
                                .review("Review 1")
                                .idPlanning(1)
                                .idRoom(1)
                                .idTeacher(1)
                                .idTeam(1)
                                .speciality("DEV")
                                .build();

                when(teacherService.getConsulting(id)).thenReturn(expectedResponse);

                mockMvc.perform(get("/teacher/getConsulting/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idConsulting").value(id))
                                .andExpect(jsonPath("$.idRoom").value(1))
                                .andExpect(jsonPath("$.idPlanning").value(1))
                                .andExpect(jsonPath("$.idTeam").value(1))
                                .andExpect(jsonPath("$.idTeacher").value(1))
                                .andExpect(jsonPath("$.review").value("Review 1"))
                                .andExpect(jsonPath("$.speciality").value("DEV"));

                verify(teacherService, times(1)).getConsulting(id);
        }

        @Test
        @WithMockUser
        void testAddDisponibility() throws Exception {
                PlanningRequest planningRequest = new PlanningRequest();
                planningRequest.setIdPlanning(1);

                mockMvc.perform(post("/teacher/addDisponibility")
                                .header("Authorization",
                                                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(new ObjectMapper().writeValueAsString(planningRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated());

                verify(teacherService, times(1)).addDisponibility(planningRequest, 9);
        }

        @Test
        @WithMockUser
        void testRemoveDisponibility() throws Exception {
                PlanningRequest planningRequest = new PlanningRequest();
                planningRequest.setIdPlanning(1);

                mockMvc.perform(post("/teacher/removeDisponibility")
                                .header("Authorization",
                                                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(objectMapper.writeValueAsString(planningRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                verify(teacherService, times(1)).removeDisponibility(planningRequest, 9);
        }

        @Test
        @WithMockUser
        void testAcceptNotification() throws Exception {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setIdNotification(1);

                mockMvc.perform(put("/teacher/acceptNotification")
                                .header("Authorization",
                                                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(new ObjectMapper().writeValueAsString(notificationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isCreated());

                verify(notificationService, times(1)).acceptConsulting(notificationRequest, 9);
        }

        @Test
        @WithMockUser
        void testRefuseNotification() throws Exception {
                NotificationRequest notificationRequest = new NotificationRequest();
                notificationRequest.setIdNotification(1);

                mockMvc.perform(put("/teacher/refuseNotification")
                                .header("Authorization",
                                                "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3BoaWUucm91c3NlYXVAZXNlby5mciIsImlzcyI6IkVTRU8iLCJpYXQiOjE2ODQyNjg4ODgsInJvbGUiOiJST0xFX09QVElPTl9MRUFERVIiLCJpZCI6OSwiZXhwIjoxNjg0MzU1Mjg4fQ.D9Gz5BCsI642zbuAnzjbtwRaQAPFbxbg96L-mbtabFeFEan5WLNd10QkfnIbbTpUgTS9E1fEXNgl_JM2-H0zVw")
                                .content(objectMapper.writeValueAsString(notificationRequest))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                verify(notificationService, times(1)).refuseConsulting(notificationRequest, 9);
        }

        @Test
        @WithMockUser
        void setcommentTeacher_shouldUpdatePresentationComments() throws Exception {
                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .idPresentation(1)
                                .commentTeacher1("CommentTeacher1")
                                .commentTeacher2("CommentTeacher2")
                                .build();

                mockMvc.perform(put("/teacher/setPresentationCommentTeacher")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk());

                verify(teacherService, times(1)).setPresentationCommentTeacher(presentationRequest);
        }

        @Test
        @WithMockUser
        void validateSpecialities_shouldReturnNoContent() throws Exception {
                StatusRequest request = StatusRequest.builder()
                                .id(1)
                                .validate(true)
                                .build();

                mockMvc.perform(put("/teacher/changeStatusSpecialities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isNoContent());

                verify(teacherService, times(1)).validateSpecialities(request);
        }

}
