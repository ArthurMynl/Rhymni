package com.projet_gl.rhymni.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.projet_gl.rhymni.dto.StudentRequest;
import com.projet_gl.rhymni.dto.StudentResponse;
import com.projet_gl.rhymni.entity.Options;
import com.projet_gl.rhymni.entity.Role;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.repository.StudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public List<StudentResponse> getAllStudents() {
        List<Student> students = studentRepository.findAll();

        return students.stream().map(this::mapToStudentResponse).collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Integer studentId) {
        Student student = studentRepository.getReferenceById(studentId);
        return mapToStudentResponse(student);
    }

    public List<StudentResponse> getStudentsTeam() {
        List<Student> students = studentRepository.findByTeamNotNull();
        return students.stream().map(this::mapToStudentResponse).collect(Collectors.toList());
    }

    public List<StudentResponse> getStudentsTeam(Integer teamId) {
        List<Student> students = studentRepository.findByTeamNumber(teamId);
        return students.stream().map(this::mapToStudentResponse).collect(Collectors.toList());
    }

    protected StudentResponse mapToStudentResponse(Student student) {
        if (student.getTeam() != null) {
            return StudentResponse.builder()
                    .id(student.getIdUser())
                    .name(student.getName())
                    .surname(student.getSurname())
                    .mailAddress(student.getMailAddress())
                    .option(student.getOption().name())
                    .idTeam(student.getTeam().getNumber())
                    .studentMark(student.getStudentMark())
                    .juryBonus(student.getJuryBonus())
                    .optionLeaderBonus(student.getOptionLeaderBonus())
                    .build();
        } else {
            return StudentResponse.builder()
                    .id(student.getIdUser())
                    .name(student.getName())
                    .surname(student.getSurname())
                    .mailAddress(student.getMailAddress())
                    .option(student.getOption().name())
                    .studentMark(student.getStudentMark())
                    .juryBonus(student.getJuryBonus())
                    .optionLeaderBonus(student.getOptionLeaderBonus())
                    .build();
        }

    }

    public HttpStatus importStudent(MultipartFile csvFile) throws CsvValidationException, NumberFormatException {
        String[] line;
        Options option;
        try {
            // parsing a CSV file into BufferedReader class constructor
            CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream()))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

            // Validate header line
            line = reader.readNext();
            if (line == null || line.length < 5 ||
                    !"name".equalsIgnoreCase(line[0]) ||
                    !"surname".equalsIgnoreCase(line[1]) ||
                    !"mailAddress".equalsIgnoreCase(line[2]) ||
                    !"optionName".equalsIgnoreCase(line[3]) ||
                    !"password".equalsIgnoreCase(line[4])) {
                reader.close();
                return HttpStatus.BAD_REQUEST;
            }

            // Process each line
            while ((line = reader.readNext()) != null) {
                if (line.length < 5) {
                    log.error("Invalid data: {}", (Object) line);
                    continue; // skip this line
                }
                log.info("{}", (Object) line);

                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String password = passwordEncoder.encode(line[4]);
                try {
                    option = Options.valueOf(line[3].toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.error("Invalid option: {}", line[3]);
                    continue; // skip this student
                }
                log.info("{}", option);
                Student student = Student.builder()
                        .name(line[0])
                        .surname(line[1])
                        .mailAddress(line[2])
                        .team(null)
                        .role(Role.ROLE_STUDENT)
                        .password(password)
                        .build();

                student.setMailAddress(line[2]);
                student.setPassword(password);
                student.setOption(option);
                studentRepository.save(student);
            }
            log.info("Students imported");
            reader.close();
            return HttpStatus.OK;
        } catch (IOException e) {
            log.error("Error reading CSV file", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public void setStudentMarks(StudentRequest studentRequest) {
        Student student = studentRepository.getReferenceById(studentRequest.getId());
        if (studentRequest.getStudentMark() != null) {
            student.setStudentMark(studentRequest.getStudentMark());
        }
        if (studentRequest.getJuryBonus() != null) {
            student.setJuryBonus(studentRequest.getJuryBonus());
        }
        if (studentRequest.getOptionLeaderBonus() != null) {
            student.setOptionLeaderBonus(studentRequest.getOptionLeaderBonus());
        }
        studentRepository.save(student);
    }

}
