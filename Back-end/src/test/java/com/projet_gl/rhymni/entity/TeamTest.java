package com.projet_gl.rhymni.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeamTest {

    private Team team1;
    private Team team2;
    private LocalDateTime dateModif;
    private LocalDateTime dateModif2;
    private Project project;
    private Project project2;
    private Student student;
    private Student student2;
    private Set<Student> listStudent;
    private Set<Student> listStudent2;

    @BeforeEach
    public void setUp() {

        dateModif = LocalDateTime.of(2023, 5, 14, 12, 0);
        dateModif2 = LocalDateTime.of(2024, 5, 14, 12, 0);

        project = new Project();
        project.setIdProject(1);
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setStatus(ProjectStatus.PENDING);

        project2 = new Project();
        project2.setIdProject(2);
        project2.setName("Project 2");
        project2.setDescription("Description 2");
        project2.setStatus(ProjectStatus.VALIDATED);

        student = Student.builder()
                .idUser(1)
                .name("ange")
                .surname("fremy")
                .mailAddress("ange.fremy@example.com")
                .option(Options.LD)
                .build();

        student2 = Student.builder()
                .idUser(2)
                .name("parthur")
                .surname("peyniel")
                .mailAddress("parthur.peyniel@example.com")
                .option(Options.LD)
                .build();

        student.setRole(Role.ROLE_STUDENT);
        student2.setRole(Role.ROLE_STUDENT);
        listStudent = new HashSet<>();
        listStudent.add(student);
        listStudent2 = new HashSet<>();
        listStudent2.add(student2);

        team1 = Team.builder()
                .number(1)
                .linkTeam(null)
                .dateModifTestBook(dateModif)
                .project(project)
                .students(listStudent)
                .linkToTestBook("my.eseo.fr")
                .build();

        team2 = Team.builder()
                .number(2)
                .linkTeam(team1)
                .dateModifTestBook(dateModif2)
                .project(project2)
                .students(listStudent2)
                .linkToTestBook("my.eseo2.fr")
                .build();
        team1.setLinkTeam(team2);
    }

    @Test
    void testGetTeam() {
        // Team
        Assertions.assertEquals(1, team1.getNumber());
        Assertions.assertEquals(dateModif, team1.getDateModifTestBook());
        Assertions.assertEquals("my.eseo.fr", team1.getLinkToTestBook());
        Assertions.assertEquals(listStudent, team1.getStudents());
        Assertions.assertEquals(project, team1.getProject());
        Assertions.assertEquals(team2, team1.getLinkTeam());

        // Project
        Assertions.assertEquals(1, team1.getProject().getIdProject());
        Assertions.assertEquals("Project 1", team1.getProject().getName());
        Assertions.assertEquals("Description 1", team1.getProject().getDescription());
        Assertions.assertEquals(project.getStatus(), team1.getProject().getStatus());

    }

    @Test
    void testSetTeam() {
        team1.setNumber(3);
        team1.setDateModifTestBook(dateModif2);
        team1.setProject(project2);
        team1.setLinkToTestBook("null.fr");
        team1.setStudents(listStudent2);

        // Team
        Assertions.assertEquals(3, team1.getNumber());
        Assertions.assertEquals(dateModif2, team1.getDateModifTestBook());
        Assertions.assertEquals("null.fr", team1.getLinkToTestBook());
        Assertions.assertEquals(listStudent2, team1.getStudents());
        Assertions.assertEquals(project2, team1.getProject());
        Assertions.assertEquals(team2, team1.getLinkTeam());

        // Project
        Assertions.assertEquals(2, team1.getProject().getIdProject());
        Assertions.assertEquals("Project 2", team1.getProject().getName());
        Assertions.assertEquals("Description 2", team1.getProject().getDescription());
        Assertions.assertEquals(project2.getStatus(), team1.getProject().getStatus());

    }
}
