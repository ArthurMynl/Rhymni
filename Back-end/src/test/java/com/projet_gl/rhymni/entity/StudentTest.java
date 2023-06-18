package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {
    private Student student;

    @BeforeEach
    public void setup() {
        student = Student.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .option(Options.LD)
                .build();
        student.setRole(Role.ROLE_STUDENT);

    }

    @Test
    void testStudentGetters() {
        student = Student.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .password("network")
                .option(Options.LD)
                .build();
        assertNotNull(student);
        assertEquals(1, student.getIdUser());
        assertEquals("Louis", student.getName());
        assertEquals("Legendre", student.getSurname());
        assertEquals("louis.legendre@example.com", student.getMailAddress());
        assertEquals(Role.ROLE_STUDENT, student.getRole());
        assertEquals(Options.LD, student.getOption());
    }

    @Test
    void testStudentTeam() {
        Team team1 = new Team();
        Team team2 = new Team();
        Project project = new Project();
        team1.setNumber(1);
        team1.setLinkTeam(team2);
        team1.setProject(project);
        student.setTeam(team1);
        assertNotNull(student.getTeam());
        assertEquals(1, student.getTeam().getNumber());
        assertEquals(team2, student.getTeam().getLinkTeam());
        assertEquals(project, student.getTeam().getProject());
    }
}
