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

import java.time.LocalDateTime;
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
import com.projet_gl.rhymni.dto.ConsultingRequest;
import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.SpecialityResponse;
import com.projet_gl.rhymni.service.ConsultingService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class ConsultingControllerTest {

    @MockBean
    private ConsultingService consultingService;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        reset(consultingService);
    }

    @Test
    @WithMockUser
    void testAskConsulting() throws Exception {
        ConsultingRequest consultingRequest = ConsultingRequest.builder()
                .idTeam(1)
                .idTeacher(1)
                .idPlanning(1)
                .hours("2023-05-08 08:20:00.000000")
                .speciality("DEV")
                .build();

        mockMvc.perform(put("/consulting/askConsulting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(consultingRequest)))
                .andExpect(status().isCreated());
        verify(consultingService, times(1)).createConsulting(consultingRequest);
    }

    @Test
    @WithMockUser
    void testGetSpecialities() throws Exception {
        SpecialityResponse specialityResponse1 = SpecialityResponse.builder()
                .specialityId(1)
                .name("DEV")
                .build();
        SpecialityResponse specialityResponse2 = SpecialityResponse.builder()
                .specialityId(2)
                .name("INF")
                .build();

        List<SpecialityResponse> specialityResponseList = Arrays.asList(specialityResponse1, specialityResponse2);

        when(consultingService.getSpecialities()).thenReturn(specialityResponseList);

        mockMvc.perform(get("/consulting/getSpecialities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].specialityId", is(specialityResponse1.getSpecialityId())))
                .andExpect(jsonPath("$[0].name", is(specialityResponse1.getName())))
                .andExpect(jsonPath("$[1].specialityId", is(specialityResponse2.getSpecialityId())))
                .andExpect(jsonPath("$[1].name", is(specialityResponse2.getName())));

        verify(consultingService, times(1)).getSpecialities();
    }

    @Test
    @WithMockUser
    void testgetAllConsultings() throws Exception {
        ConsultingResponse consultingResponse1 = ConsultingResponse.builder()
                .idConsulting(1)
                .idTeam(1)
                .idTeacher(1)
                .idRoom(1)
                .idPlanning(1)
                .review("consulting")
                .speciality("DEV")
                .build();
        ConsultingResponse consultingResponse2 = ConsultingResponse.builder()
                .idConsulting(2)
                .idTeam(2)
                .idTeacher(2)
                .idRoom(2)
                .idPlanning(2)
                .review("new consulting")
                .speciality("INF")
                .build();

        List<ConsultingResponse> consultingResponsesList = Arrays.asList(consultingResponse1, consultingResponse2);

        when(consultingService.getAllConsultings()).thenReturn(consultingResponsesList);

        mockMvc.perform(get("/consulting/getAllPlanned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].idConsulting", is(consultingResponse1.getIdConsulting())))
                .andExpect(jsonPath("$[0].idPlanning", is(consultingResponse1.getIdPlanning())))
                .andExpect(jsonPath("$[0].idTeam", is(consultingResponse1.getIdTeam())))
                .andExpect(jsonPath("$[0].idRoom", is(consultingResponse1.getIdRoom())))
                .andExpect(jsonPath("$[0].review", is(consultingResponse1.getReview())))
                .andExpect(jsonPath("$[0].idTeacher", is(consultingResponse1.getIdTeacher())))
                .andExpect(jsonPath("$[0].speciality", is(consultingResponse1.getSpeciality())))

                .andExpect(jsonPath("$[1].idConsulting", is(consultingResponse2.getIdConsulting())))
                .andExpect(jsonPath("$[1].idPlanning", is(consultingResponse2.getIdPlanning())))
                .andExpect(jsonPath("$[1].idTeam", is(consultingResponse2.getIdTeam())))
                .andExpect(jsonPath("$[1].idRoom", is(consultingResponse2.getIdRoom())))
                .andExpect(jsonPath("$[1].review", is(consultingResponse2.getReview())))
                .andExpect(jsonPath("$[1].idTeacher", is(consultingResponse2.getIdTeacher())))
                .andExpect(jsonPath("$[1].speciality", is(consultingResponse2.getSpeciality())));

        verify(consultingService, times(1)).getAllConsultings();

    }

    @Test
    @WithMockUser
    void testGetAllConsultingsForTeacher() throws Exception {
        ConsultingResponse consultingResponse1 = ConsultingResponse.builder()
                .idConsulting(1)
                .idTeam(1)
                .idTeacher(1)
                .idRoom(1)
                .idPlanning(1)
                .review("consulting")
                .speciality("DEV")
                .build();

        List<ConsultingResponse> consultingResponsesList = Arrays.asList(consultingResponse1);

        when(consultingService.getAllConsultingsForTeacher(1)).thenReturn(consultingResponsesList);

        mockMvc.perform(get("/consulting/getAllForTeacher/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].idConsulting", is(consultingResponse1.getIdConsulting())))
                .andExpect(jsonPath("$[0].idPlanning", is(consultingResponse1.getIdPlanning())))
                .andExpect(jsonPath("$[0].idTeam", is(consultingResponse1.getIdTeam())))
                .andExpect(jsonPath("$[0].idRoom", is(consultingResponse1.getIdRoom())))
                .andExpect(jsonPath("$[0].review", is(consultingResponse1.getReview())))
                .andExpect(jsonPath("$[0].idTeacher", is(consultingResponse1.getIdTeacher())))
                .andExpect(jsonPath("$[0].speciality", is(consultingResponse1.getSpeciality())));

        verify(consultingService, times(1)).getAllConsultingsForTeacher(1);

    }

    @Test
    @WithMockUser
    void testGetAllAvailableSlots() throws Exception {
        LocalDateTime startDate = LocalDateTime.of(2023, 5, 14, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 14, 12, 0);
        List<Integer> listTeacherId = Arrays.asList(1, 2);
        PlanningResponse planningResponse = PlanningResponse.builder()
                .dateEnd(endDate)
                .dateStart(startDate)
                .teachersID(listTeacherId)
                .idPlanning(1)
                .build();

        List<PlanningResponse> planningResponsesList = Arrays.asList(planningResponse);

        when(consultingService.getAllAvailableSlots()).thenReturn(planningResponsesList);

        mockMvc.perform(get("/consulting/getAllAvailableSlots"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dateEnd").value("2024-05-14T12:00:00"))
                .andExpect(jsonPath("$[0].idPlanning", is(planningResponse.getIdPlanning())))
                .andExpect(jsonPath("$[0].dateStart").value("2023-05-14T12:00:00"))
                .andExpect(jsonPath("$[0].teachersID", is(planningResponse.getTeachersID())));

        verify(consultingService, times(1)).getAllAvailableSlots();
    }

}
