package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.PresentationResponse;
import com.projet_gl.rhymni.dto.RoomResponse;
import com.projet_gl.rhymni.dto.SemesterInfoRequest;
import com.projet_gl.rhymni.dto.SemesterInfoResponse;
import com.projet_gl.rhymni.service.PlanningAssistantService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class PlanningAssistantControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PlanningAssistantService planningAssistantService;

        @BeforeEach
        void setUp() {
                Mockito.reset(planningAssistantService);
        }

        @Test
        @WithMockUser
        void viewAllPresentation_shouldReturnListOfPresentations() throws Exception {
                List<PresentationResponse> presentations = Arrays.asList(
                                PresentationResponse.builder()
                                                .idPresentation(1)
                                                .type("Type 1")
                                                .dateHours(LocalDateTime.of(2023, 5, 15, 12, 0))
                                                .commentTeam1("Comment 1")
                                                .commentTeacher1("Comment 2")
                                                .commentTeam2("Comment 1")
                                                .commentTeacher2("Comment 2")
                                                .build(),
                                PresentationResponse.builder()
                                                .idPresentation(2)
                                                .type("Type 2")
                                                .dateHours(LocalDateTime.of(2023, 5, 16, 14, 0))
                                                .commentTeam1("Comment 3")
                                                .commentTeacher1("Comment 4")
                                                .commentTeam2("Comment 3")
                                                .commentTeacher2("Comment 4")
                                                .build());
                when(planningAssistantService.getAllPresentations()).thenReturn(presentations);

                mockMvc.perform(get("/planning/getPresentations")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].idPresentation", is(1)))
                                .andExpect(jsonPath("$[0].type", is("Type 1")))
                                .andExpect(jsonPath("$[0].dateHours", is("2023-05-15T12:00:00")))
                                .andExpect(jsonPath("$[0].commentTeam1", is("Comment 1")))
                                .andExpect(jsonPath("$[0].commentTeacher1", is("Comment 2")))
                                .andExpect(jsonPath("$[0].commentTeam2", is("Comment 1")))
                                .andExpect(jsonPath("$[0].commentTeacher2", is("Comment 2")))
                                .andExpect(jsonPath("$[1].idPresentation", is(2)))
                                .andExpect(jsonPath("$[1].type", is("Type 2")))
                                .andExpect(jsonPath("$[1].dateHours", is("2023-05-16T14:00:00")))
                                .andExpect(jsonPath("$[1].commentTeam1", is("Comment 3")))
                                .andExpect(jsonPath("$[1].commentTeacher1", is("Comment 4")))
                                .andExpect(jsonPath("$[1].commentTeam2", is("Comment 3")))
                                .andExpect(jsonPath("$[1].commentTeacher2", is("Comment 4")));

                verify(planningAssistantService, times(1)).getAllPresentations();
        }

        @Test
        @WithMockUser
        void viewAllRoom_shouldReturnListOfRooms() throws Exception {
                // Given
                List<RoomResponse> rooms = Arrays.asList(
                                RoomResponse.builder().numberRoom(1).name("Room 1").build(),
                                RoomResponse.builder().numberRoom(2).name("Room 2").build());
                when(planningAssistantService.getAllRooms()).thenReturn(rooms);

                // When and Then
                mockMvc.perform(get("/planning/getRooms")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].numberRoom", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Room 1")))
                                .andExpect(jsonPath("$[1].numberRoom", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Room 2")));
        }

        @Test
        @WithMockUser
        void createIntermediairePresentation_shouldReturnSuccessResponse() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.createIntermediairePresentation(any())).thenReturn(
                                ResponseEntity.ok().body("Soutenance intermédiaire créée avec succès."));

                // When and Then
                mockMvc.perform(post("/planning/createIntermediatePresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Soutenance intermédiaire créée avec succès."));

                verify(planningAssistantService, times(1)).createIntermediairePresentation(any());
        }

        @Test
        @WithMockUser
        void createIntermediairePresentation_shouldReturnBadRequest() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                // Set invalid presentationRequest data here

                when(planningAssistantService.createIntermediairePresentation(any())).thenReturn(
                                ResponseEntity.badRequest().body("Error message"));

                // When and Then
                mockMvc.perform(post("/planning/createIntermediatePresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Error message"));

                verify(planningAssistantService, times(1)).createIntermediairePresentation(any());
        }

        @Test
        @WithMockUser
        void createIntermediairePresentation_shouldReturnConflictStatus() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.createIntermediairePresentation(any())).thenReturn(
                                ResponseEntity.status(HttpStatus.CONFLICT).body("Error message"));

                // When and Then
                mockMvc.perform(post("/planning/createIntermediatePresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isConflict())
                                .andExpect(content().string("Error message"));

                verify(planningAssistantService, times(1)).createIntermediairePresentation(any());
        }

        @Test
        @WithMockUser
        void createFinalPresentation_shouldReturnSuccessResponse() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.createFinalPresentation(any())).thenReturn(
                                ResponseEntity.ok().body("Soutenance finale créée avec succès."));

                // When and Then
                mockMvc.perform(post("/planning/createFinalPresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Soutenance finale créée avec succès."));

                verify(planningAssistantService, times(1)).createFinalPresentation(any());
        }

        @Test
        @WithMockUser
        void createFinalPresentation_shouldReturnBadRequest() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                // Set invalid presentationRequest data here

                when(planningAssistantService.createFinalPresentation(any())).thenReturn(
                                ResponseEntity.badRequest().body("Error message"));

                // When and Then
                mockMvc.perform(post("/planning/createFinalPresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Error message"));

                verify(planningAssistantService, times(1)).createFinalPresentation(any());
        }

        @Test
        @WithMockUser
        void createFinalPresentation_shouldReturnConflictStatus() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.createFinalPresentation(any())).thenReturn(
                                ResponseEntity.status(HttpStatus.CONFLICT).body("Error message"));

                // When and Then
                mockMvc.perform(post("/planning/createFinalPresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isConflict())
                                .andExpect(content().string("Error message"));

                verify(planningAssistantService, times(1)).createFinalPresentation(any());
        }

        @Test
        @WithMockUser
        void deleteIntermediairePresentation_shouldReturnSuccessResponse() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdPresentation(1);
                presentationRequest.setIdTeam(1);

                when(planningAssistantService.deletePresentation(any())).thenReturn(
                                ResponseEntity.ok().body("Soutenance intermédiaire supprimée avec succès."));

                // When and Then
                mockMvc.perform(post("/planning/deleteIntermediatePresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Soutenance intermédiaire supprimée avec succès."));

                verify(planningAssistantService, times(1)).deletePresentation(any());
        }

        @Test
        @WithMockUser
        void getPlanning_shouldReturnPlanningResponse() throws Exception {
                // Given
                PlanningResponse planningResponse1 = new PlanningResponse();
                planningResponse1.setIdPlanning(1);
                planningResponse1.setDateStart(LocalDateTime.of(2023, 5, 15, 0, 0, 0));
                planningResponse1.setDateEnd(LocalDateTime.of(2023, 5, 16, 0, 0, 0));
                planningResponse1.setTeachersID(Arrays.asList(1, 2, 3));

                PlanningResponse planningResponse2 = new PlanningResponse();
                // Set more planningResponse data here

                List<PlanningResponse> planningResponses = Arrays.asList(planningResponse1, planningResponse2);

                when(planningAssistantService.getAllPlanning()).thenReturn(planningResponses);

                // When and Then
                mockMvc.perform(get("/planning/getPlanning"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].idPlanning").value(1))
                                .andExpect(jsonPath("$[0].dateStart").value("2023-05-15T00:00:00"))
                                .andExpect(jsonPath("$[0].dateEnd").value("2023-05-16T00:00:00"))
                                .andExpect(jsonPath("$[0].teachersID").isArray())
                                .andExpect(jsonPath("$[0].teachersID", hasSize(3)))
                                .andExpect(jsonPath("$[0].teachersID", containsInAnyOrder(1, 2, 3)));
                verify(planningAssistantService, times(1)).getAllPlanning();
        }

        @Test
        @WithMockUser
        void getAllPlanningForTeacher_shouldReturnPlanningResponse() throws Exception {
                // Given
                int teacherId = 1;

                PlanningResponse planningResponse1 = new PlanningResponse();
                planningResponse1.setIdPlanning(1);
                planningResponse1.setDateStart(LocalDateTime.of(2023, 5, 15, 0, 0, 0));
                planningResponse1.setDateEnd(LocalDateTime.of(2023, 5, 16, 0, 0, 0));
                planningResponse1.setTeachersID(Arrays.asList(1, 2, 3));

                PlanningResponse planningResponse2 = new PlanningResponse();
                // Set more planningResponse data here

                List<PlanningResponse> planningResponses = Arrays.asList(planningResponse1, planningResponse2);

                when(planningAssistantService.getAllPlanningForTeacher(teacherId)).thenReturn(planningResponses);

                // When and Then
                mockMvc.perform(get("/planning/getAllPlanningsTeacher/{id}", teacherId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].idPlanning").value(1))
                                .andExpect(jsonPath("$[0].dateStart").value("2023-05-15T00:00:00"))
                                .andExpect(jsonPath("$[0].dateEnd").value("2023-05-16T00:00:00"))
                                .andExpect(jsonPath("$[0].teachersID").isArray())
                                .andExpect(jsonPath("$[0].teachersID", hasSize(3)))
                                .andExpect(jsonPath("$[0].teachersID", containsInAnyOrder(1, 2, 3)));

                verify(planningAssistantService, times(1)).getAllPlanningForTeacher(teacherId);
        }

        @Test
        @WithMockUser
        void testImportConsultingTimeSlot() throws Exception {
                // Prepare the test data
                MockMultipartFile csvFile = new MockMultipartFile("file", "filename.csv", "text/csv",
                                "DateDebut;DateFin\n2023-04-23 08:00;2023-04-23 12:00\n2023-04-24 08:00;2023-04-24 12:00"
                                                .getBytes());

                // Configure the mock service
                when(planningAssistantService.importConsultingTimeSlot(any(MultipartFile.class)))
                                .thenReturn(HttpStatus.CREATED);

                // Perform the POST request and assert the response
                mockMvc.perform(multipart("/planning/import").file(csvFile))
                                .andExpect(status().isCreated());

                // Verify that the service method was called with the correct arguments
                verify(planningAssistantService, times(1)).importConsultingTimeSlot(any(MultipartFile.class));
        }

        @Test
        @WithMockUser
        void modifyIntermediairePresentation_shouldReturnSuccessResponse() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdPresentation(1);
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.modifyIntermediairePresentation(any())).thenReturn(
                                ResponseEntity.ok().body("Soutenance intermédiaire modifiée avec succès."));

                // When and Then
                mockMvc.perform(post("/planning/modifyIntermediatePresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Soutenance intermédiaire modifiée avec succès."));

                verify(planningAssistantService, times(1)).modifyIntermediairePresentation(any());
        }

        @Test
        @WithMockUser
        void modifyFinalPresentation_shouldReturnSuccessResponse() throws Exception {
                // Given
                PresentationRequest presentationRequest = new PresentationRequest();
                presentationRequest.setIdPresentation(1);
                presentationRequest.setIdTeacher(1);
                presentationRequest.setIdTeam(1);
                presentationRequest.setDate("2023-05-15");
                presentationRequest.setHours("12:00");
                presentationRequest.setIdRoom(1);

                when(planningAssistantService.modifyFinalPresentation(any())).thenReturn(
                                ResponseEntity.ok().body("Soutenance finale modifiée avec succès."));

                // When and Then
                mockMvc.perform(post("/planning/modifyFinalPresentation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(presentationRequest)))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Soutenance finale modifiée avec succès."));

                verify(planningAssistantService, times(1)).modifyFinalPresentation(any());
        }

        @Test
        @WithMockUser
        void updateStartProject() throws Exception {
        // Given
        SemesterInfoRequest semesterRequest = new SemesterInfoRequest();
        // Set up the necessary attributes in the semesterRequest object

        // When and Then
        mockMvc.perform(put("/planning/updateStartProject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(semesterRequest)))
                .andExpect(status().isOk());

        verify(planningAssistantService, times(1)).updateStartDateProject(semesterRequest);
        }

        @Test
        @WithMockUser
        void updateEndProject() throws Exception {
        // Given
        SemesterInfoRequest semesterRequest = new SemesterInfoRequest();
        // Set up the necessary attributes in the semesterRequest object

        // When and Then
        mockMvc.perform(put("/planning/updateEndProject")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(semesterRequest)))
                .andExpect(status().isOk());

        verify(planningAssistantService, times(1)).updateEndDateProject(semesterRequest);
        }

        @Test
        @WithMockUser
        void getSemesterActual() throws Exception {
            // Given
            SemesterInfoResponse expectedResponse = new SemesterInfoResponse();
            // Set up the expected response data
        
            when(planningAssistantService.getSemesterActual()).thenReturn(expectedResponse);
        
            // When and Then
            mockMvc.perform(get("/planning/semester"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
        
            verify(planningAssistantService, times(1)).getSemesterActual();
        }
        
}
