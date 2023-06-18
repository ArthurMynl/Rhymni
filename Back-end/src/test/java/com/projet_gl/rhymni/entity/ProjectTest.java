package com.projet_gl.rhymni.entity;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProjectTest {

    private Project project;

    @BeforeEach
    public void setUp() {
        project = new Project();
        project.setIdProject(1);
        project.setName("Project 1");
        project.setDescription("Description 1");
        project.setStatus(ProjectStatus.PENDING);
    }

    @Test
    void testProjectGetters() {
        Assertions.assertEquals(1, project.getIdProject());
        Assertions.assertEquals("Project 1", project.getName());
        Assertions.assertEquals("Description 1", project.getDescription());
        Assertions.assertEquals(ProjectStatus.PENDING, project.getStatus());
    }

    @Test
    void testProjectTeam() {
        Team team = new Team();
        team.setNumber(1);
        project.setTeam(team);
        Assertions.assertEquals(team, project.getTeam());
    }

    @Test
    void testProjectTeachers() {
        Set<Teacher> teachers = new HashSet<>();
        Teacher teacher1 = new Teacher();
        teacher1.setIdUser(1);
        teacher1.setName("Louis");
        teacher1.setSurname("Legendre");
        teacher1.setMailAddress("louis.legendre@example.com");
        teachers.add(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setIdUser(2);
        teacher2.setName("Lou");
        teacher2.setSurname("legen");
        teacher2.setMailAddress("lou.legen@example.com");
        teachers.add(teacher2);

        project.setTeachers(teachers);
        Assertions.assertEquals(2, project.getTeachers().size());
        Assertions.assertTrue(project.getTeachers().contains(teacher1));
        Assertions.assertTrue(project.getTeachers().contains(teacher2));
    }
}
