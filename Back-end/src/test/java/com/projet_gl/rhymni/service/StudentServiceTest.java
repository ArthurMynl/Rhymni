package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvValidationException;
import com.projet_gl.rhymni.dto.StudentRequest;
import com.projet_gl.rhymni.dto.StudentResponse;
import com.projet_gl.rhymni.entity.Options;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .password("network")
                .option(Options.LD)
                .build();
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findAll()).thenReturn(students);

        List<StudentResponse> studentResponses = studentService.getAllStudents();

        assertEquals(1, studentResponses.size());
        assertEquals(student.getIdUser(), studentResponses.get(0).getId());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.getReferenceById(1)).thenReturn(student);

        StudentResponse studentResponse = studentService.getStudentById(1);

        assertEquals(student.getIdUser(), studentResponse.getId());
        assertEquals(student.getName(), studentResponse.getName());
        assertEquals(student.getSurname(), studentResponse.getSurname());
        assertEquals(student.getMailAddress(), studentResponse.getMailAddress());
        assertEquals(student.getOption().name(), studentResponse.getOption());
        verify(studentRepository, times(1)).getReferenceById(1);
    }

    @Test
    void testGetStudentsTeam() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findByTeamNotNull()).thenReturn(students);

        List<StudentResponse> studentResponses = studentService.getStudentsTeam();

        assertEquals(1, studentResponses.size());
        assertEquals(student.getIdUser(), studentResponses.get(0).getId());
        verify(studentRepository, times(1)).findByTeamNotNull();
    }

    @Test
    void testGetStudentsTeamWithId() {
        List<Student> students = Arrays.asList(student);
        when(studentRepository.findByTeamNumber(1)).thenReturn(students);

        List<StudentResponse> studentResponses = studentService.getStudentsTeam(1);

        assertEquals(1, studentResponses.size());
        assertEquals(student.getIdUser(), studentResponses.get(0).getId());
        verify(studentRepository, times(1)).findByTeamNumber(1);
    }

    @Test
    void testImportStudent() throws CsvValidationException, IOException {
        // Create a mock MultipartFile object
        byte[] fileContent = "name;surname;mailAddress;optionName;password\nJohn;Doe;john.doe@example.com;CSS;password1\nJane;Doe;jane.doe@example.com;LD;password2"
                .getBytes();
        MultipartFile csvFile = new MockMultipartFile("test.csv", fileContent);

        // Mock the behavior of the StudentRepository
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Call the importStudent function
        HttpStatus result = studentService.importStudent(csvFile);

        // Verify the result
        assertEquals(HttpStatus.OK, result);
        verify(studentRepository, times(2)).save(any(Student.class));
    }

    @Test
    void testImportStudent_HeaderNotEqualToName() throws CsvValidationException, IOException {
        // Create a mock MultipartFile object with a CSV file content
        byte[] fileContent = "invalidHeader;surname;mailAddress;optionName;password\nJohn;Doe;john.doe@example.com;CSS;password1"
                .getBytes();
        MultipartFile csvFile = new MockMultipartFile("test.csv", fileContent);

        // Call the importStudent function
        HttpStatus result = studentService.importStudent(csvFile);

        // Verify the result
        assertEquals(HttpStatus.BAD_REQUEST, result);
        verifyNoInteractions(studentRepository);
    }

    @Test
    void testSetStudentMarks() {
        int studentId = 1;
        float studentMark = 8.5f;
        float juryBonus = 1.0f;
        float optionLeaderBonus = 0.5f;
        StudentRequest studentRequest = StudentRequest.builder()
                .id(studentId)
                .studentMark(studentMark)
                .juryBonus(juryBonus)
                .optionLeaderBonus(optionLeaderBonus)
                .build();

        when(studentRepository.getReferenceById(studentId)).thenReturn(student);

        studentService.setStudentMarks(studentRequest);

        assertEquals(studentMark, student.getStudentMark());
        assertEquals(juryBonus, student.getJuryBonus());
        assertEquals(optionLeaderBonus, student.getOptionLeaderBonus());
        verify(studentRepository, times(1)).save(student);
    }

}
