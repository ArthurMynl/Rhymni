package com.projet_gl.rhymni.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsultingTest {

    private Teacher teacher;
    private Consulting consulting;
    private Room room;
    private Planning planning;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Set<Consulting> listConsulting;
    private Set<Teacher> listTeacher;
    private Set<Speciality> specialities;
    private Set<Consulting> listConsulting2;
    private Set<Teacher> listTeacher2;
    private Set<Speciality> specialities2;
    private Consulting consulting2;
    private Room room2;
    private Planning planning2;
    private Teacher teacher2;
    private LocalDateTime startDate2;
    private LocalDateTime endDate2;

    @BeforeEach
    public void setUp() {

        specialities = new HashSet<>();
        specialities.add(new Speciality(1, "TWIC"));
        specialities.add(new Speciality(2, "Security"));
        teacher = Teacher.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .specialities(specialities)
                .build();

        startDate = LocalDateTime.of(2023, 5, 14, 12, 0);
        endDate = LocalDateTime.of(2024, 5, 14, 12, 0);
        planning = Planning.builder()
                .idPlanning(1)
                .dateStart(startDate)
                .dateEnd(endDate)
                .build();

        room = Room.builder()
                .numberRoom(1)
                .name("Tesla")
                .build();

        consulting = Consulting.builder()
                .idConsulting(1)
                .idTeam(1)
                .review("consulting")
                .specialityConsulting("DEV")
                .planning(planning)
                .room(room)
                .teacher(teacher)
                .build();

        listConsulting = new HashSet<>();
        listConsulting.add(consulting);
        planning.setConsultings(listConsulting);
        room.setConsultings(listConsulting);
        listTeacher = new HashSet<>();
        listTeacher.add(teacher);
        planning.setTeachers(listTeacher);
        teacher.setConsultings(listConsulting);
    }

    @Test
    void testGetConsulting() {
        // Consulting
        Assertions.assertEquals(1, consulting.getIdConsulting());
        Assertions.assertEquals(1, consulting.getIdTeam());
        Assertions.assertEquals("DEV", consulting.getSpecialityConsulting());
        Assertions.assertEquals("consulting", consulting.getReview());
        Assertions.assertEquals(room, consulting.getRoom());
        Assertions.assertEquals(planning, consulting.getPlanning());
        Assertions.assertEquals(teacher, consulting.getTeacher());

        // Room
        Assertions.assertEquals(1, consulting.getRoom().getNumberRoom());
        Assertions.assertEquals("Tesla", consulting.getRoom().getName());
        Assertions.assertEquals(listConsulting, consulting.getRoom().getConsultings());
        Assertions.assertEquals(null, consulting.getRoom().getPresentations());

        // planning
        Assertions.assertEquals(1, consulting.getPlanning().getIdPlanning());
        Assertions.assertEquals(startDate, consulting.getPlanning().getDateStart());
        Assertions.assertEquals(endDate, consulting.getPlanning().getDateEnd());
        Assertions.assertEquals(listConsulting, consulting.getPlanning().getConsultings());
        Assertions.assertEquals(listTeacher, consulting.getPlanning().getTeachers());

        // Teacher
        Assertions.assertEquals(1, consulting.getTeacher().getIdUser());
        Assertions.assertEquals(listConsulting, consulting.getTeacher().getConsultings());
        Assertions.assertEquals("louis.legendre@example.com", consulting.getTeacher().getMailAddress());
        Assertions.assertEquals("Louis", consulting.getTeacher().getName());
        Assertions.assertEquals("Legendre", consulting.getTeacher().getSurname());
        Assertions.assertEquals(specialities, consulting.getTeacher().getSpecialities());

    }

    @Test
    void testSetConsulting() {
        consulting2 = Consulting.builder()
                .idConsulting(1)
                .idTeam(1)
                .review("consulting")
                .specialityConsulting("DEV")
                .planning(planning)
                .room(room)
                .teacher(teacher)
                .build();
        consulting2.setIdConsulting(2);
        consulting2.setIdTeam(2);
        consulting2.setSpecialityConsulting("INF");
        consulting2.setReview("new consulting");
        startDate2 = LocalDateTime.of(2023, 5, 14, 12, 0);
        endDate2 = LocalDateTime.of(2024, 5, 14, 12, 0);
        planning2 = Planning.builder()
                .idPlanning(2)
                .dateStart(startDate2)
                .dateEnd(endDate2)
                .build();

        room2 = Room.builder()
                .numberRoom(2)
                .name("Edison")
                .build();

        specialities2 = new HashSet<>();
        specialities2.add(new Speciality(1, "Cyber"));
        specialities2.add(new Speciality(2, "SDSI"));
        teacher2 = Teacher.builder()
                .idUser(2)
                .name("Ange")
                .surname("Fremy")
                .mailAddress("ange.fremy@example.com")
                .specialities(specialities2)
                .build();
        consulting2.setTeacher(teacher2);
        consulting2.setRoom(room2);
        consulting2.setPlanning(planning2);
        listConsulting = new HashSet<>();
        listConsulting.add(consulting2);
        planning2.setConsultings(listConsulting2);
        room2.setConsultings(listConsulting2);
        listTeacher2 = new HashSet<>();
        listTeacher2.add(teacher2);
        planning2.setTeachers(listTeacher2);
        teacher2.setConsultings(listConsulting2);

        // Consulting
        Assertions.assertEquals(2, consulting2.getIdConsulting());
        Assertions.assertEquals(2, consulting2.getIdTeam());
        Assertions.assertEquals("INF", consulting2.getSpecialityConsulting());
        Assertions.assertEquals("new consulting", consulting2.getReview());

        // Room
        Assertions.assertEquals(2, consulting2.getRoom().getNumberRoom());
        Assertions.assertEquals("Edison", consulting2.getRoom().getName());
        Assertions.assertEquals(listConsulting2, consulting2.getRoom().getConsultings());
        Assertions.assertEquals(null, consulting2.getRoom().getPresentations());

        // planning
        Assertions.assertEquals(2, consulting2.getPlanning().getIdPlanning());
        Assertions.assertEquals(startDate2, consulting2.getPlanning().getDateStart());
        Assertions.assertEquals(endDate2, consulting2.getPlanning().getDateEnd());
        Assertions.assertEquals(listConsulting2, consulting2.getPlanning().getConsultings());
        Assertions.assertEquals(listTeacher2, consulting2.getPlanning().getTeachers());

        // Teacher
        Assertions.assertEquals(2, consulting2.getTeacher().getIdUser());
        Assertions.assertEquals(listConsulting2, consulting2.getTeacher().getConsultings());
        Assertions.assertEquals("ange.fremy@example.com", consulting2.getTeacher().getMailAddress());
        Assertions.assertEquals("Ange", consulting2.getTeacher().getName());
        Assertions.assertEquals("Fremy", consulting2.getTeacher().getSurname());
        Assertions.assertEquals(specialities2, consulting2.getTeacher().getSpecialities());

    }

}
