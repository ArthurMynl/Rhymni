package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.ProjectValidationRequest;
import com.projet_gl.rhymni.dto.PairTeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.service.ProjectService;
import com.projet_gl.rhymni.service.TeamService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class OptionLeaderControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private TeamService teamService;

        @MockBean
        private ProjectService projectService;

        @AfterEach
        void tearDown() {
                reset(teamService, projectService);
        }

        @Test
        @WithMockUser
        void createTeamPair_shouldReturnCreated() throws Exception {
                PairTeamRequest pairTeamRequest = PairTeamRequest.builder()
                                .idLinkedTeam1(1)
                                .idLinkedTeam2(2)
                                .build();

                mockMvc.perform(post("/optionLeader/createTeamPair")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                pairTeamRequest)))
                                .andExpect(status().isCreated());

                verify(teamService).createTeamPair(pairTeamRequest);
        }

        @Test
        @WithMockUser
        void deleteTeamPair_shouldReturnNoContent() throws Exception {
                PairTeamRequest pairTeamRequest = PairTeamRequest.builder()
                                .idLinkedTeam1(1)
                                .idLinkedTeam2(2)
                                .build();

                mockMvc.perform(post("/optionLeader/deleteTeamPair")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                pairTeamRequest)))
                                .andExpect(status().isNoContent());

                verify(teamService).deleteTeamPair(pairTeamRequest);
        }

        @Test
        @WithMockUser
        void delegateValidationProject_shouldReturnNoContent() throws Exception {
                ProjectValidationRequest projectValidationRequest = ProjectValidationRequest.builder()
                                .idProject(1)
                                .idTeacher(2)
                                .build();

                mockMvc.perform(put("/optionLeader/delegateValidationProject")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(
                                                projectValidationRequest)))
                                .andExpect(status().isNoContent());

                verify(projectService).delegateValidationProject(projectValidationRequest);
        }

        @Test
        @WithMockUser
        void getTeamPair_shouldReturnOk() throws Exception {
                TeamResponse teamResponse1 = TeamResponse.builder()
                                .number(1)
                                .idProject(1)
                                .linkedTeamNumber(2)
                                .build();

                TeamResponse teamResponse2 = TeamResponse.builder()
                                .number(2)
                                .idProject(2)
                                .linkedTeamNumber(1)
                                .build();

                List<List<TeamResponse>> teamPairs = Arrays.asList(Arrays.asList(teamResponse1, teamResponse2));

                when(teamService.getTeamPair()).thenReturn(teamPairs);

                mockMvc.perform(get("/optionLeader/getTeamPair"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(1)))
                                .andExpect(jsonPath("$[0][0].number", is(teamResponse1.getNumber())))
                                .andExpect(jsonPath("$[0][0].idProject", is(teamResponse1.getIdProject())))
                                .andExpect(jsonPath("$[0][0].linkedTeamNumber",
                                                is(teamResponse1.getLinkedTeamNumber())))
                                .andExpect(jsonPath("$[0][1].number", is(teamResponse2.getNumber())))
                                .andExpect(jsonPath("$[0][1].idProject", is(teamResponse2.getIdProject())))
                                .andExpect(jsonPath("$[0][1].linkedTeamNumber",
                                                is(teamResponse2.getLinkedTeamNumber())));

                verify(teamService, times(1)).getTeamPair();
        }

        @Test
        @WithMockUser
        void getAllTeams_shouldReturnOk() throws Exception {
                TeamResponse teamResponse1 = TeamResponse.builder()
                                .number(1)
                                .idProject(1)
                                .linkedTeamNumber(2)
                                .build();

                TeamResponse teamResponse2 = TeamResponse.builder()
                                .number(2)
                                .idProject(2)
                                .linkedTeamNumber(1)
                                .build();

                List<TeamResponse> allTeams = Arrays.asList(teamResponse1, teamResponse2);

                when(teamService.getAllTeams()).thenReturn(allTeams);

                mockMvc.perform(get("/optionLeader/getAllTeams"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[0].number", is(teamResponse1.getNumber())))
                                .andExpect(jsonPath("$[0].idProject", is(teamResponse1.getIdProject())))
                                .andExpect(jsonPath("$[0].linkedTeamNumber", is(teamResponse1.getLinkedTeamNumber())))
                                .andExpect(jsonPath("$[1].number", is(teamResponse2.getNumber())))
                                .andExpect(jsonPath("$[1].idProject", is(teamResponse2.getIdProject())))
                                .andExpect(jsonPath("$[1].linkedTeamNumber", is(teamResponse2.getLinkedTeamNumber())));

                verify(teamService, times(1)).getAllTeams();
        }

}