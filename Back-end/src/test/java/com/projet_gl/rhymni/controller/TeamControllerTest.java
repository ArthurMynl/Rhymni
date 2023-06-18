package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.LinkedTeamResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.service.TeamService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class TeamControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TeamService teamService;

        @Autowired
        private ObjectMapper objectMapper;

        @AfterEach
        void tearDown() {
                reset(teamService);
        }

        @Test
        @WithMockUser
        void testViewAllTeams() throws Exception {
                TeamResponse teamResponse1 = TeamResponse.builder()
                                .idProject(1)
                                .number(1)
                                .build();
                TeamResponse teamResponse2 = TeamResponse.builder()
                                .idProject(2)
                                .number(2)
                                .build();
                List<TeamResponse> teamResponses = Arrays.asList(teamResponse1, teamResponse2);

                when(teamService.getAllTeams()).thenReturn(teamResponses);

                mockMvc.perform(get("/team/getTeams"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].number", is(teamResponse1.getNumber())))
                                .andExpect(jsonPath("$[0].idProject", is(teamResponse1.getIdProject())))
                                .andExpect(jsonPath("$[1].number", is(teamResponse2.getNumber())))
                                .andExpect(jsonPath("$[1].idProject", is(teamResponse2.getIdProject())));

                verify(teamService, times(1)).getAllTeams();
        }

        @Test
        @WithMockUser
        void testGetLinkedTeam() throws Exception {
                int teamId = 1;
                LinkedTeamResponse linkedTeamResponse = LinkedTeamResponse.builder()
                                .idTeam(1)
                                .idLinkedTeam(2)
                                .build();

                when(teamService.getLinkedTeam(teamId)).thenReturn(linkedTeamResponse);

                mockMvc.perform(get("/team/getLinkedTeam/{id}", teamId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.idTeam", is(linkedTeamResponse.getIdTeam())))
                                .andExpect(jsonPath("$.idLinkedTeam", is(linkedTeamResponse.getIdLinkedTeam())));

                verify(teamService, times(1)).getLinkedTeam(teamId);
        }

        @Test
        @WithMockUser
        void testGetTeamByProject() throws Exception {
                int projectId = 1;
                TeamResponse teamResponse = TeamResponse.builder()
                                .idProject(projectId)
                                .number(1)
                                .build();

                when(teamService.getTeamByProject(projectId)).thenReturn(teamResponse);

                mockMvc.perform(get("/team/getTeamByProject/{id}", projectId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.number", is(teamResponse.getNumber())))
                                .andExpect(jsonPath("$.idProject", is(teamResponse.getIdProject())));

                verify(teamService, times(1)).getTeamByProject(projectId);
        }

        @Test
        @WithMockUser
        void testGetFreeTeams() throws Exception {
                TeamResponse teamResponse1 = TeamResponse.builder()
                                .idProject(1)
                                .number(1)
                                .build();
                TeamResponse teamResponse2 = TeamResponse.builder()
                                .idProject(2)
                                .number(2)
                                .build();
                List<TeamResponse> teamResponses = Arrays.asList(teamResponse1, teamResponse2);

                when(teamService.getUnpairedTeams()).thenReturn(teamResponses);

                mockMvc.perform(get("/team/getUnpairedTeams"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].number", is(teamResponse1.getNumber())))
                                .andExpect(jsonPath("$[0].idProject", is(teamResponse1.getIdProject())))
                                .andExpect(jsonPath("$[1].number", is(teamResponse2.getNumber())))
                                .andExpect(jsonPath("$[1].idProject", is(teamResponse2.getIdProject())));

                verify(teamService, times(1)).getUnpairedTeams();
        }

        @Test
        @WithMockUser
        void setcommentTeam_shouldUpdatePresentationComments() throws Exception {
                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .idPresentation(1)
                                .commentTeam1("CommentTeam1")
                                .commentTeam2("CommentTeam2")
                                .build();

                mockMvc.perform(put("/team/setPresentationCommentTeam")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk());

                verify(teamService, times(1)).setcommentTeam(presentationRequest);
        }

        @Test
        @WithMockUser
        void testGetTeamByNumber() throws Exception {
                int teamNumber = 1;
                TeamResponse teamResponse = TeamResponse.builder()
                                .number(teamNumber)
                                .build();

                when(teamService.getTeamByNumber(teamNumber)).thenReturn(teamResponse);

                mockMvc.perform(get("/team/getTeamByNumber/{number}", teamNumber))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.number", is(teamResponse.getNumber())));

                verify(teamService, times(1)).getTeamByNumber(teamNumber);
        }

        @Test
        @WithMockUser
        void testSetMarkTeam() throws Exception {
                TeamRequest teamRequest = TeamRequest.builder()
                                .teamNumber(1)
                                .markPresentation(9.5f)
                                .markValidation(8.2f)
                                .build();

                mockMvc.perform(put("/team/setMarkTeam")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(teamRequest)))
                                .andExpect(status().isOk());

                verify(teamService, times(1)).setMarkTeam(teamRequest);
        }

}
