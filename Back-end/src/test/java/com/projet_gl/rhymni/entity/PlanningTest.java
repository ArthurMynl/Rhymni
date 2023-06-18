package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class PlanningTest {

    @Test
    void testCreatePlanning() {
        // Create a Planning object
        Planning planning = new Planning();
        planning.setIdPlanning(1);
        planning.setDateStart(LocalDateTime.of(2023, 5, 1, 9, 0));
        planning.setDateEnd(LocalDateTime.of(2023, 5, 1, 17, 0));

        // Create a set of Consulting objects
        Set<Consulting> consultings = new HashSet<>();
        Consulting consulting1 = new Consulting();
        consulting1.setIdConsulting(1);
        // Set other properties for consulting1
        consultings.add(consulting1);

        // Create a set of Teacher objects
        Set<Teacher> teachers = new HashSet<>();
        Teacher teacher1 = new Teacher();
        teacher1.setIdUser(1);
        // Set other properties for teacher1
        teachers.add(teacher1);

        // Set the sets of consultings and teachers for the planning
        planning.setConsultings(consultings);
        planning.setTeachers(teachers);

        // Perform assertions to validate the created Planning object
        assertEquals(1, planning.getIdPlanning());
        assertEquals(LocalDateTime.of(2023, 5, 1, 9, 0), planning.getDateStart());
        assertEquals(LocalDateTime.of(2023, 5, 1, 17, 0), planning.getDateEnd());
        assertEquals(1, planning.getConsultings().size());
        assertEquals(1, planning.getTeachers().size());

        // Additional assertions for the sets of consultings and teachers
        Consulting consulting = planning.getConsultings().iterator().next();
        assertEquals(1, consulting.getIdConsulting());

        Teacher teacher = planning.getTeachers().iterator().next();
        assertEquals(1, teacher.getIdUser());
    }

}
