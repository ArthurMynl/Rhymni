package com.projet_gl.rhymni.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamStudentRequest;
import com.projet_gl.rhymni.entity.ProjectStatus;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.service.ProjectService;
import com.projet_gl.rhymni.service.TeamService;

import io.jsonwebtoken.Claims;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class TeamMemberControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private ProjectService projectService;

        @MockBean
        private TeamService teamService;

        @Test
        @WithMockUser
        void testUpdateDescription() throws Exception {
                DescriptionRequest descriptionRequest = DescriptionRequest.builder()
                                .id(1)
                                .description("New description")
                                .build();

                mockMvc.perform(put("/teamMember/updateDescription")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(descriptionRequest)))
                                .andExpect(status().isNoContent());

                verify(projectService, times(1)).updateDescription(descriptionRequest);
        }

        @Test
        @WithMockUser
        void testGetProjects() throws Exception {
                List<ProjectResponse> projectResponses = new ArrayList<>();
                projectResponses.add(ProjectResponse.builder()
                                .id(1)
                                .name("Project 1")
                                .description("Description 1")
                                .status(ProjectStatus.PENDING)
                                .build());
                projectResponses.add(ProjectResponse.builder()
                                .id(2)
                                .name("Project 2")
                                .description("Description 2")
                                .status(ProjectStatus.VALIDATED)
                                .build());

                when(projectService.getProjects()).thenReturn(projectResponses);

                mockMvc.perform(get("/teamMember/getProjects"))
                                .andExpect(status().isOk())
                                .andExpect(content().json(objectMapper.writeValueAsString(projectResponses)));

                verify(projectService, times(1)).getProjects();
        }

        @Test
        @WithMockUser
        void testModifyTestBook() throws Exception {
                TeamRequest teamRequest = TeamRequest.builder()
                                .teamNumber(1)
                                .idProject(1)
                                .linkedTeamNumber(2)
                                .linkTestBook("http://linktotestbook.com")
                                .dateModifTest(LocalDateTime.now())
                                .build();

                doNothing().when(teamService).modifyTestBook(teamRequest);

                mockMvc.perform(put("/teamMember/modifyTestBook")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(teamRequest)))
                                .andExpect(status().isNoContent());

                verify(teamService, times(1)).modifyTestBook(teamRequest);
        }

        @Test
        @WithMockUser
        void testRegisterStudentTeam() throws Exception {
                TeamStudentRequest teamStudentRequest = TeamStudentRequest.builder()
                                                        .idTeam(1)
                                                        .build();

                Claims claims = mock(Claims.class);
                when(claims.get("id")).thenReturn(123);

                Student student = Student.builder()
                                        .idUser(123)
                                        .build();

                doNothing().when(teamService).registerStudent(teamStudentRequest, student.getIdUser());

                mockMvc.perform(put("/registerStudent")
                        .header("Authorization", "Bearer token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(teamStudentRequest)))
                        .andExpect(status().isOk());

                verify(teamService, times(1)).registerStudent(teamStudentRequest, student.getIdUser());
        }

        @Test
        @WithMockUser
        void testRemoveStudentTeam() throws Exception {
                TeamStudentRequest teamStudentRequest = TeamStudentRequest.builder()
                                                        .idTeam(1)
                                                        .build();

                Claims claims = mock(Claims.class);
                when(claims.get("id")).thenReturn(123); // ID de l'étudiant

                Student student = Student.builder()
                                        .idUser(123)
                                        .build();

                doNothing().when(teamService).removeStudent(teamStudentRequest, student.getIdUser());

                mockMvc.perform(put("/removeStudent")
                        .header("Authorization", "Bearer token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(teamStudentRequest)))
                        .andExpect(status().isOk());

                verify(teamService, times(1)).removeStudent(teamStudentRequest, student.getIdUser());
        }
}
