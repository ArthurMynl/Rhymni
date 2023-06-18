package com.projet_gl.rhymni.entity;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    public void setUp() {

        Set<Speciality> specialities = new HashSet<>();
        specialities.add(new Speciality(1, "TWIC"));
        specialities.add(new Speciality(2, "Security"));
        teacher = Teacher.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .specialities(specialities)
                .build();
    }

    @Test
    void testTeacherGetters() {
        Assertions.assertEquals(1, teacher.getIdUser());
        Assertions.assertEquals("Louis", teacher.getName());
        Assertions.assertEquals("Legendre", teacher.getSurname());
        Assertions.assertEquals("louis.legendre@example.com", teacher.getMailAddress());
        Assertions.assertEquals(Role.ROLE_TEACHER, teacher.getRole());
        Assertions.assertEquals(2, teacher.getSpecialities().size());
    }

    @Test
    void testTeacherLinkTeacher() {
        Teacher linkTeacher = new Teacher();
        teacher.setLinkTeacher(linkTeacher);
        Assertions.assertEquals(linkTeacher, teacher.getLinkTeacher());
    }

    @Test
    void testTeacherProjects() {
        Set<Project> projects = new HashSet<>();
        Project project1 = new Project();
        project1.setIdProject(1);
        project1.setName("Projet 1");
        project1.setDescription("Description 1");
        project1.setStatus(ProjectStatus.PENDING);

        Project project2 = new Project();
        project2.setIdProject(2);
        project2.setName("Projet 2");
        project2.setDescription("Description 2");
        project2.setStatus(ProjectStatus.PENDING);

        projects.add(project1);
        projects.add(project2);
        teacher.setProjects(projects);
        Assertions.assertEquals(2, teacher.getProjects().size());
        Assertions.assertTrue(teacher.getProjects().contains(project1));
        Assertions.assertTrue(teacher.getProjects().contains(project2));
    }

    @Test
    void testTeacherSpecialities() {

        Speciality newSpeciality = new Speciality(3, "TWIC");
        teacher.getSpecialities().add(newSpeciality);
        Assertions.assertEquals(3, teacher.getSpecialities().size());
        Assertions.assertTrue(teacher.getSpecialities().contains(newSpeciality));

        teacher.getSpecialities().remove(newSpeciality);
        Assertions.assertEquals(2, teacher.getSpecialities().size());
        Assertions.assertFalse(teacher.getSpecialities().contains(newSpeciality));
    }
}
