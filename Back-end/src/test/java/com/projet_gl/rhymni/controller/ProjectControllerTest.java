package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
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
import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.ProjectStatusResponse;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.entity.ProjectStatus;
import com.projet_gl.rhymni.service.ProjectService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ProjectControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProjectService projectService;

        @AfterEach
        void tearDown() {
                reset(projectService);
        }

        @Test
        @WithMockUser
        void updateDescription_shouldReturnNoContent() throws Exception {
                DescriptionRequest descriptionRequest = DescriptionRequest.builder()
                                .id(1)
                                .description("New description")
                                .build();

                mockMvc.perform(put("/project/updateDescription")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(descriptionRequest)))
                                .andExpect(status().isNoContent());

                verify(projectService, times(1)).updateDescription(descriptionRequest);
        }

        @Test
        @WithMockUser
        void getProjects_shouldReturnListOfProjects() throws Exception {

                List<ProjectResponse> projectResponses = new ArrayList<>();

                ProjectResponse project1 = ProjectResponse.builder()
                                .id(1)
                                .name("Project 1")
                                .description("Description 1")
                                .status(ProjectStatus.PENDING)
                                .build();
                projectResponses.add(project1);

                ProjectResponse project2 = ProjectResponse.builder()
                                .id(2)
                                .name("Project 2")
                                .description("Description 2")
                                .status(ProjectStatus.VALIDATED)
                                .build();
                projectResponses.add(project2);

                when(projectService.getProjects()).thenReturn(projectResponses);

                mockMvc.perform(get("/project/getProjects"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Project 1")))
                                .andExpect(jsonPath("$[0].description", is("Description 1")))
                                .andExpect(jsonPath("$[0].status", is(ProjectStatus.PENDING.toString())))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Project 2")))
                                .andExpect(jsonPath("$[1].description", is("Description 2")))
                                .andExpect(jsonPath("$[1].status", is(ProjectStatus.VALIDATED.toString())));

                verify(projectService, times(1)).getProjects();
        }

        @Test
        @WithMockUser
        void getProjectById_shouldReturnProjectById() throws Exception {

                int projectId = 1;

                ProjectResponse project1 = ProjectResponse.builder()
                                .id(projectId)
                                .name("Project 1")
                                .description("Description 1")
                                .status(ProjectStatus.PENDING)
                                .build();

                when(projectService.getProject(projectId)).thenReturn(project1);

                mockMvc.perform(get("/project/getProject/{projectId}", projectId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(projectId)))
                                .andExpect(jsonPath("$.name", is("Project 1")))
                                .andExpect(jsonPath("$.description", is("Description 1")))
                                .andExpect(jsonPath("$.status", is(ProjectStatus.PENDING.toString())))
                                .andReturn();
                verify(projectService, times(1)).getProject(projectId);
        }

        @Test
        @WithMockUser
        void validateProject_shouldReturnNoContent() throws Exception {
                StatusRequest request = StatusRequest.builder()
                                .id(1)
                                .validate(true)
                                .build();

                mockMvc.perform(put("/project/changeStatus")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                                .andExpect(status().isNoContent());

                verify(projectService, times(1)).validateProject(request);
        }

        @Test
        @WithMockUser
        void getProjectStatus_returnsProjectStatusResponse() throws Exception {
                int projectId = 1;
                ProjectStatusResponse expectedResponse = ProjectStatusResponse.builder()
                                .idProject(projectId)
                                .status(ProjectStatus.PENDING)
                                .build();

                when(projectService.getProjectStatus(projectId)).thenReturn(expectedResponse);

                mockMvc.perform(get("/project/getProjectStatus/{projectId}", projectId))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.idProject", is(projectId)))
                                .andExpect(jsonPath("$.status", is(ProjectStatus.PENDING.toString())));

                verify(projectService, times(1)).getProjectStatus(projectId);
        }

     

}
