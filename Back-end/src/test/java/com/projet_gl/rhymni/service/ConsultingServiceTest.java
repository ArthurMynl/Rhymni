package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projet_gl.rhymni.dto.ConsultingRequest;
import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.SpecialityResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.SpecialityRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class ConsultingServiceTest {

    @InjectMocks
    private ConsultingService consultingService;

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PlanningRepository planningRepository;

    @Mock
    private ConsultingRepository consultingRepository;

    @Test
    void getAllConsultings_shouldReturnAllConsultings() {
        // Arrange
        List<Consulting> consultingList = new ArrayList<>();
        Consulting consulting1 = new Consulting();
        consulting1.setIdConsulting(1);
        consulting1.setIdTeam(1);
        consulting1.setTeacher(new Teacher(1, null, null, null, null, null));
        consulting1.setPlanning(new Planning(1, null, null, null, null));
        consulting1.setSpecialityConsulting("Speciality 1");
        consulting1.setReview("Review 1");
        consultingList.add(consulting1);
        Consulting consulting2 = new Consulting();
        consulting2.setIdConsulting(2);
        consulting2.setIdTeam(2);
        consulting2.setTeacher(new Teacher(2, null, null, null, null, null));
        consulting2.setPlanning(new Planning(2, null, null, null, null));
        consulting2.setSpecialityConsulting("Speciality 2");
        consulting2.setReview("Review 2");
        consultingList.add(consulting2);
        when(consultingRepository.findAll()).thenReturn(consultingList);

        // Act
        List<ConsultingResponse> responseList = consultingService.getAllConsultings();

        // Assert
        assertEquals(2, responseList.size());
        assertEquals(1, responseList.get(0).getIdConsulting());
        assertEquals(1, responseList.get(0).getIdTeam());
        assertEquals(1, responseList.get(0).getIdTeacher());
        assertEquals(1, responseList.get(0).getIdPlanning());
        assertEquals("Speciality 1", responseList.get(0).getSpeciality());
        assertEquals("Review 1", responseList.get(0).getReview());
        assertEquals(2, responseList.get(1).getIdConsulting());
        assertEquals(2, responseList.get(1).getIdTeam());
        assertEquals(2, responseList.get(1).getIdTeacher());
        assertEquals(2, responseList.get(1).getIdPlanning());
        assertEquals("Speciality 2", responseList.get(1).getSpeciality());
        assertEquals("Review 2", responseList.get(1).getReview());
    }

    @Test
    void getSpecialities_shouldReturnAllSpecialities() {
        // Arrange
        List<Speciality> specialityList = new ArrayList<>();
        specialityList.add(new Speciality(1, "Speciality 1"));
        specialityList.add(new Speciality(2, "Speciality 2"));
        when(specialityRepository.findAll()).thenReturn(specialityList);

        // Act
        List<SpecialityResponse> responseList = consultingService.getSpecialities();

        // Assert
        assertEquals(2, responseList.size());
        assertEquals(1, responseList.get(0).getSpecialityId());
        assertEquals("Speciality 1", responseList.get(0).getName());
        assertEquals(2, responseList.get(1).getSpecialityId());
        assertEquals("Speciality 2", responseList.get(1).getName());
    }

    @Test
    void createConsulting_shouldSaveConsultingToRepository() {
        // Arrange
        ConsultingRequest consultingRequest = new ConsultingRequest();
        consultingRequest.setIdTeam(1);
        consultingRequest.setIdTeacher(2);
        consultingRequest.setIdPlanning(3);
        consultingRequest.setSpeciality("Speciality");

        Team team = new Team();
        team.setNumber(1);
        when(teamRepository.getReferenceById(1)).thenReturn(team);

        Planning planning = new Planning();
        planning.setIdPlanning(3);
        when(planningRepository.getReferenceById(3)).thenReturn(planning);

        // Act
        consultingService.createConsulting(consultingRequest);

        // Assert
        verify(consultingRepository, times(1)).save(any(Consulting.class));
    }

    @Test
    void getAllConsultingsForTeacher_shouldReturnConsultingsForTeacher() {
        // Arrange
        int idTeacher = 1;
        List<Consulting> consultingList = new ArrayList<>();
        Consulting consulting1 = new Consulting();
        consulting1.setIdConsulting(1);
        consulting1.setIdTeam(1);
        consulting1.setTeacher(new Teacher(1, null, null, null, null, null));
        consulting1.setPlanning(new Planning(1, null, null, null, null));
        consulting1.setSpecialityConsulting("Speciality 1");
        consulting1.setReview("Review 1");
        consultingList.add(consulting1);
        Consulting consulting2 = new Consulting();
        consulting2.setIdConsulting(2);
        consulting2.setIdTeam(2);
        consulting2.setTeacher(new Teacher(2, null, null, null, null, null));
        consulting2.setPlanning(new Planning(2, null, null, null, null));
        consulting2.setSpecialityConsulting("Speciality 2");
        consulting2.setReview("Review 2");
        consultingList.add(consulting2);
        when(consultingRepository.findAllByTeacherIdUser(idTeacher)).thenReturn(consultingList);

        // Act
        List<ConsultingResponse> responseList = consultingService.getAllConsultingsForTeacher(idTeacher);

        // Assert
        assertEquals(2, responseList.size());
        assertEquals(1, responseList.get(0).getIdConsulting());
        assertEquals(1, responseList.get(0).getIdTeam());
        assertEquals(1, responseList.get(0).getIdTeacher());
        assertEquals(1, responseList.get(0).getIdPlanning());
        assertEquals("Speciality 1", responseList.get(0).getSpeciality());
        assertEquals("Review 1", responseList.get(0).getReview());
        assertEquals(2, responseList.get(1).getIdConsulting());
        assertEquals(2, responseList.get(1).getIdTeam());
        assertEquals(2, responseList.get(1).getIdTeacher());
        assertEquals(2, responseList.get(1).getIdPlanning());
        assertEquals("Speciality 2", responseList.get(1).getSpeciality());
        assertEquals("Review 2", responseList.get(1).getReview());
    }

    @Test
    void getAllAvailableSlots_shouldReturnAllAvailableSlots() {
        // Arrange
        List<Planning> planningList = new ArrayList<>();
        Planning planning1 = new Planning();
        planning1.setIdPlanning(1);
        planning1.setDateStart(LocalDateTime.of(2023, 5, 1, 9, 0));
        planning1.setDateEnd(LocalDateTime.of(2023, 5, 1, 10, 0));
        Set<Teacher> teachers1 = new HashSet<>();
        teachers1.add(new Teacher(1, "John Doe", null, null, null, null));
        planning1.setTeachers(teachers1);
        planningList.add(planning1);

        Planning planning2 = new Planning();
        planning2.setIdPlanning(2);
        planning2.setDateStart(LocalDateTime.of(2023, 5, 2, 14, 0));
        planning2.setDateEnd(LocalDateTime.of(2023, 5, 2, 15, 0));
        Set<Teacher> teachers2 = new HashSet<>();
        teachers2.add(new Teacher(2, "Jane Smith", null, null, null, null));
        planning2.setTeachers(teachers2);
        planningList.add(planning2);

        when(planningRepository.findAll()).thenReturn(planningList);

        // Act
        List<PlanningResponse> responseList = consultingService.getAllAvailableSlots();

        // Assert
        assertEquals(2, responseList.size());

        assertEquals(1, responseList.get(0).getIdPlanning());
        assertEquals(LocalDateTime.of(2023, 5, 1, 9, 0), responseList.get(0).getDateStart());
        assertEquals(LocalDateTime.of(2023, 5, 1, 10, 0), responseList.get(0).getDateEnd());
        assertEquals(1, responseList.get(0).getTeachersID().size());
        assertEquals(1, responseList.get(0).getTeachersID().get(0).intValue());

        assertEquals(2, responseList.get(1).getIdPlanning());
        assertEquals(LocalDateTime.of(2023, 5, 2, 14, 0), responseList.get(1).getDateStart());
        assertEquals(LocalDateTime.of(2023, 5, 2, 15, 0), responseList.get(1).getDateEnd());
        assertEquals(1, responseList.get(1).getTeachersID().size());
        assertEquals(2, responseList.get(1).getTeachersID().get(0).intValue());
    }

}
