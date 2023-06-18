package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projet_gl.rhymni.dto.StudentRequest;
import com.projet_gl.rhymni.dto.StudentResponse;
import com.projet_gl.rhymni.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class StudentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private StudentService studentService;

        @AfterEach
        void tearDown() {
                reset(studentService);
        }

        @Test
        @WithMockUser
        void viewAllStudents_shouldReturnAllStudents() throws Exception {
                // Given
                List<StudentResponse> students = new ArrayList<>();
                students.add(StudentResponse.builder()
                                .id(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .idTeam(1)
                                .option("TWIC")
                                .build());
                students.add(StudentResponse.builder()
                                .id(2)
                                .name("Lou")
                                .surname("Legend")
                                .mailAddress("lou.legend@example.com")
                                .idTeam(2)
                                .option("CYBERSECU")
                                .build());

                when(studentService.getAllStudents()).thenReturn(students);

                // When and Then
                mockMvc.perform(get("/student/getStudents")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Louis")))
                                .andExpect(jsonPath("$[0].surname", is("Legendre")))
                                .andExpect(jsonPath("$[0].mailAddress", is("louis.legendre@example.com")))
                                .andExpect(jsonPath("$[0].idTeam", is(1)))
                                .andExpect(jsonPath("$[0].option", is("TWIC")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Lou")))
                                .andExpect(jsonPath("$[1].surname", is("Legend")))
                                .andExpect(jsonPath("$[1].mailAddress", is("lou.legend@example.com")))
                                .andExpect(jsonPath("$[1].idTeam", is(2)))
                                .andExpect(jsonPath("$[1].option", is("CYBERSECU")));

                verify(studentService, times(1)).getAllStudents();
        }

        @Test
        @WithMockUser
        void viewStudentById_shouldReturnStudentWithGivenId() throws Exception {
                // Given
                StudentResponse student = StudentResponse.builder()
                                .id(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .idTeam(1)
                                .option("TWIC")
                                .build();
                when(studentService.getStudentById(1)).thenReturn(student);

                // When and Then
                mockMvc.perform(get("/student/getStudent/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id", is(1)))
                                .andExpect(jsonPath("$.name", is("Louis")))
                                .andExpect(jsonPath("$.surname", is("Legendre")))
                                .andExpect(jsonPath("$.mailAddress", is("louis.legendre@example.com")))
                                .andExpect(jsonPath("$.idTeam", is(1)))
                                .andExpect(jsonPath("$.option", is("TWIC")));

                verify(studentService, times(1)).getStudentById(1);
        }

        @Test
        @WithMockUser
        void viewAllStudentsInTeam_shouldReturnAllStudentsInTeam() throws Exception {
                // Given
                List<StudentResponse> students = new ArrayList<>();
                students.add(StudentResponse.builder()
                                .id(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .idTeam(1)
                                .option("TWIC")
                                .build());
                students.add(StudentResponse.builder()
                                .id(2)
                                .name("Lou")
                                .surname("Legend")
                                .mailAddress("lou.legend@example.com")
                                .idTeam(1)
                                .option("CYBERSECU")
                                .build());
                students.add(StudentResponse.builder()
                                .id(3)
                                .name("Lo")
                                .surname("Leg")
                                .mailAddress("lo.leg@example.com")
                                .idTeam(2)
                                .option("TWIC")
                                .build());

                when(studentService.getStudentsTeam()).thenReturn(students);

                // When and Then
                mockMvc.perform(get("/student/getStudentsInTeam")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Louis")))
                                .andExpect(jsonPath("$[0].surname", is("Legendre")))
                                .andExpect(jsonPath("$[0].mailAddress", is("louis.legendre@example.com")))
                                .andExpect(jsonPath("$[0].idTeam", is(1)))
                                .andExpect(jsonPath("$[0].option", is("TWIC")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Lou")))
                                .andExpect(jsonPath("$[1].surname", is("Legend")))
                                .andExpect(jsonPath("$[1].mailAddress", is("lou.legend@example.com")))
                                .andExpect(jsonPath("$[1].idTeam", is(1)))
                                .andExpect(jsonPath("$[1].option", is("CYBERSECU")))
                                .andExpect(jsonPath("$[2].id", is(3)))
                                .andExpect(jsonPath("$[2].name", is("Lo")))
                                .andExpect(jsonPath("$[2].surname", is("Leg")))
                                .andExpect(jsonPath("$[2].mailAddress", is("lo.leg@example.com")))
                                .andExpect(jsonPath("$[2].idTeam", is(2)))
                                .andExpect(jsonPath("$[2].option", is("TWIC")));

                verify(studentService, times(1)).getStudentsTeam();
        }

        @Test
        @WithMockUser
        void viewTeamStudentById_shouldReturnStudentsInTeamById() throws Exception {
                // Given
                List<StudentResponse> students = new ArrayList<>();
                students.add(StudentResponse.builder()
                                .id(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .idTeam(1)
                                .option("TWIC")
                                .build());
                students.add(StudentResponse.builder()
                                .id(2)
                                .name("Lou")
                                .surname("Legend")
                                .mailAddress("lou.legend@example.com")
                                .idTeam(1)
                                .option("CYBERSECU")
                                .build());

                when(studentService.getStudentsTeam(1)).thenReturn(students);

                // When and Then
                mockMvc.perform(get("/student/getStudentsInTeam/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].id", is(1)))
                                .andExpect(jsonPath("$[0].name", is("Louis")))
                                .andExpect(jsonPath("$[0].surname", is("Legendre")))
                                .andExpect(jsonPath("$[0].mailAddress", is("louis.legendre@example.com")))
                                .andExpect(jsonPath("$[0].idTeam", is(1)))
                                .andExpect(jsonPath("$[0].option", is("TWIC")))
                                .andExpect(jsonPath("$[1].id", is(2)))
                                .andExpect(jsonPath("$[1].name", is("Lou")))
                                .andExpect(jsonPath("$[1].surname", is("Legend")))
                                .andExpect(jsonPath("$[1].mailAddress", is("lou.legend@example.com")))
                                .andExpect(jsonPath("$[1].idTeam", is(1)))
                                .andExpect(jsonPath("$[1].option", is("CYBERSECU")));

                verify(studentService, times(1)).getStudentsTeam(1);
        }

        @Test
        @WithMockUser
        void importStudent_shouldReturnHttpStatusCreated() throws Exception {
                // Given
                MockMultipartFile csvFile = new MockMultipartFile("file", "test.csv", "text/csv",
                                "id,name,surname,mailAddress,idTeam,option\n1,Louis,Legendre,louis.legendre@example.com,1,TWIC\n"
                                                .getBytes());

                when(studentService.importStudent(any(MultipartFile.class))).thenReturn(HttpStatus.CREATED);

                // When and Then
                mockMvc.perform(multipart("/student/import").file(csvFile))
                                .andExpect(status().isCreated());

                verify(studentService, times(1)).importStudent(any(MultipartFile.class));
        }

        @Test
        @WithMockUser
        void testSetStudentMarks() throws Exception {
                StudentRequest studentRequest = StudentRequest.builder()
                                .id(1)
                                .studentMark(8.5f)
                                .juryBonus(1.0f)
                                .optionLeaderBonus(0.5f)
                                .build();

                mockMvc.perform(put("/student/setStudentMarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(studentRequest)))
                                .andExpect(status().isOk());

                verify(studentService, times(1)).setStudentMarks(studentRequest);
        }
}
