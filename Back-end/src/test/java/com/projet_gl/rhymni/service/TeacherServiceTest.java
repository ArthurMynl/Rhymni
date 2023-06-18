package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projet_gl.rhymni.dto.PlanningRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.ReviewRequest;
import com.projet_gl.rhymni.dto.SpecialityRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.dto.TeacherRequest;
import com.projet_gl.rhymni.dto.TeacherResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Room;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.SpecialityRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @InjectMocks
    private TeacherService teacherService;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PresentationRepository presentationRepository;

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private ConsultingRepository consultingRepository;

    @Mock
    private PlanningRepository planningRepository;

    private Teacher teacher;
    private Teacher teacher1;
    private Teacher teacher2;
    private Speciality speciality1;
    private Speciality speciality2;
    private Consulting consulting1;
    private Consulting consulting2;
    private Room room;
    private Planning planning;

    @BeforeEach
    void setUp() {
        teacher1 = Teacher.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .specialities(new HashSet<>())
                .build();

        teacher2 = Teacher.builder()
                .idUser(2)
                .name("Lou")
                .surname("Legend")
                .mailAddress("lou.legend@example.com")
                .specialities(new HashSet<>())
                .build();

        speciality1 = new Speciality(1, "MOD");
        speciality2 = new Speciality(2, "INF");

    }

    @Test
    void getTeacher_shouldReturnTeacherResponse() {
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        TeacherResponse teacherResponse = teacherService.getTeacher(1);
        assertEquals(teacher1.getIdUser(), teacherResponse.getId());
        assertEquals(teacher1.getName(), teacherResponse.getName());
        assertEquals(teacher1.getSurname(), teacherResponse.getSurname());
        assertEquals(teacher1.getMailAddress(), teacherResponse.getMailAddress());
    }

    @Test
    void addSpeciality_shouldAddSpecialityToTeacher() {
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(specialityRepository.getReferenceById(1)).thenReturn(speciality1);
        SpecialityRequest specialityRequest = new SpecialityRequest(1,1);
        teacherService.addSpeciality(specialityRequest);

        assertTrue(teacher1.getSpecialities().contains(speciality1));
        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(specialityRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).save(teacher1);
    }

    @Test
    void removeSpeciality_shouldRemoveSpecialityFromTeacher() {
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(specialityRepository.getReferenceById(1)).thenReturn(speciality1);
        SpecialityRequest specialityRequest = new SpecialityRequest(1,1);
        teacher1.getSpecialities().add(speciality1);
        teacherService.removeSpeciality(specialityRequest);

        assertFalse(teacher1.getSpecialities().contains(speciality1));
        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(specialityRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).save(teacher1);
    }

    @Test
    void mergeSpecialities_shouldMergeSpecialitiesFromTwoTeachers() {
        teacher1.getSpecialities().add(speciality1);
        teacher2.getSpecialities().add(speciality2);

        Set<Speciality> mergedSpecialities = teacherService.mergeSpecialities(teacher1, teacher2);

        assertEquals(2, mergedSpecialities.size());
        assertTrue(mergedSpecialities.contains(speciality1));
        assertTrue(mergedSpecialities.contains(speciality2));
    }

    @Test
    void extractSpecialityNames_shouldExtractSpecialityNames() {
        Set<Speciality> specialities = new HashSet<>(Arrays.asList(speciality1, speciality2));

        Set<String> specialityNames = teacherService.extractSpecialityNames(specialities);

        assertEquals(2, specialityNames.size());
        assertTrue(specialityNames.contains("MOD"));
        assertTrue(specialityNames.contains("INF"));
    }

    @Test
    void areEligibleForPairing_shouldReturnFalseForNonEligiblePair() {

        teacher1.getSpecialities().add(speciality1);
        teacher2.getSpecialities().add(speciality1);

        Set<String> specialityNames = new HashSet<>(Arrays.asList("MOD"));

        assertFalse(teacherService.areEligibleForPairing(teacher1, teacher2, specialityNames));
    }

    @Test
    void unlinkExistingPairs_shouldUnlinkPairs() {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));
        teacher1.setLinkTeacher(teacher2);
        teacher2.setLinkTeacher(teacher1);

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        teacherService.unlinkExistingPairs(teacher1, teacher2);

        assertNull(teacher1.getLinkTeacher());
        assertNull(teacher2.getLinkTeacher());
        verify(teacherRepository, times(1)).findAll();
        verify(teacherRepository, times(1)).save(teacher1);
        verify(teacherRepository, times(1)).save(teacher2);
    }

    @Test
    void linkTeachers_shouldLinkTeachers() {
        teacherService.linkTeachers(teacher1, teacher2);

        assertEquals(teacher1.getLinkTeacher(), teacher2);
        assertEquals(teacher2.getLinkTeacher(), teacher1);
        verify(teacherRepository, times(1)).save(teacher1);
        verify(teacherRepository, times(1)).save(teacher2);
    }

    @Test
void getAllTeachers_shouldReturnAllTeachers() {
    when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

    List<TeacherResponse> teacherResponses = teacherService.getAllTeachers();

    assertEquals(2, teacherResponses.size());
    verify(teacherRepository, times(1)).findAll();
}

    @Test
    void getAllPairTeacher_shouldReturnAllTeacherPairs() {
        teacher1.setLinkTeacher(teacher2);
        teacher2.setLinkTeacher(teacher1);

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        List<List<TeacherResponse>> teacherPairs = teacherService.getAllPairTeacher();

        assertEquals(1, teacherPairs.size());
        verify(teacherRepository, times(1)).findAll();
    }

    @Test
    void findLinkedTeacher_shouldReturnLinkedTeacher() {
        teacher1.setLinkTeacher(teacher2);
        teacher2.setLinkTeacher(teacher1);

        Optional<Teacher> linkedTeacher = teacherService.findLinkedTeacher(Arrays.asList(teacher1, teacher2), teacher1);

        assertTrue(linkedTeacher.isPresent());
        assertEquals(teacher2, linkedTeacher.get());
    }

    @Test
    void isPairAlreadyAdded_shouldReturnTrueForExistingPair() {
        List<List<TeacherResponse>> linkedTeachers = new ArrayList<>();
        linkedTeachers.add(Arrays.asList(TeacherResponse.builder().id(teacher1.getIdUser()).build(),
                TeacherResponse.builder().id(teacher2.getIdUser()).build()));

        boolean result = teacherService.isPairAlreadyAdded(linkedTeachers, teacher1, teacher2);

        assertTrue(result);
    }

    @Test
    void isPairAlreadyAdded_shouldReturnFalseForNonExistingPair() {
        List<List<TeacherResponse>> linkedTeachers = new ArrayList<>();

        boolean result = teacherService.isPairAlreadyAdded(linkedTeachers, teacher1, teacher2);

        assertFalse(result);
    }

    @Test
    void mapToTeacherResponse_shouldMapTeacherToTeacherResponse() {
        TeacherResponse teacherResponse = teacherService.mapToTeacherResponse(teacher1);

        assertEquals(teacher1.getIdUser(), teacherResponse.getId());
        assertEquals(teacher1.getName(), teacherResponse.getName());
        assertEquals(teacher1.getSurname(), teacherResponse.getSurname());
        assertEquals(teacher1.getMailAddress(), teacherResponse.getMailAddress());
    }

    @Test
    void createPairOfTeacher_success() {
        TeacherRequest teacherRequest = new TeacherRequest(1, 2);
        teacher1.getSpecialities().add(speciality1);
        teacher2.getSpecialities().add(speciality2);
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(teacherRepository.getReferenceById(2)).thenReturn(teacher2);

        teacherService.createPairOfTeacher(teacherRequest);

        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).getReferenceById(2);
        verify(teacherRepository, times(1)).save(teacher1);
        verify(teacherRepository, times(1)).save(teacher2);
    }

    @Test
    void createPairOfTeacher_failure() {
        TeacherRequest teacherRequest = new TeacherRequest(1, 2);
        teacher1.getSpecialities().add(speciality1);
        teacher2.getSpecialities().add(speciality1);
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(teacherRepository.getReferenceById(2)).thenReturn(teacher2);

        teacherService.createPairOfTeacher(teacherRequest);

        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).getReferenceById(2);
        verify(teacherRepository, times(0)).save(teacher1);
        verify(teacherRepository, times(0)).save(teacher2);
    }

    @Test
    void testUpdateReviewService() {
        consulting1 = Consulting.builder()
                .idConsulting(1)
                .idTeam(1)
                .review("consulting")
                .specialityConsulting("DEV")
                .build();

        when(consultingRepository.getReferenceById(1)).thenReturn(consulting1);

        ReviewRequest updateRequest = new ReviewRequest("New description", 1);

        teacherService.updateReview(updateRequest);

        verify(consultingRepository, times(1)).getReferenceById(1);
        verify(consultingRepository, times(1)).save(consulting1);

    }

    @Test
    void testGetConsultingByIdService() {
        LocalDateTime startDate = LocalDateTime.of(2023, 5, 14, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 14, 12, 0);
        planning = Planning.builder()
                .idPlanning(1)
                .dateStart(startDate)
                .dateEnd(endDate)
                .build();

        room = Room.builder()
                .numberRoom(1)
                .name("Tesla")
                .build();

        consulting1 = Consulting.builder()
                .idConsulting(1)
                .idTeam(1)
                .review("consulting")
                .specialityConsulting("DEV")
                .planning(planning)
                .room(room)
                .teacher(teacher1)
                .build();

        Set<Consulting> listConsulting = new HashSet<>();
        listConsulting.add(consulting1);
        planning.setConsultings(listConsulting);
        when(consultingRepository.getReferenceById(1)).thenReturn(consulting1);

        teacherService.getConsulting(1);

        verify(consultingRepository, times(1)).getReferenceById(1);
    }

    @Test
    void testGetConsultingsService() {
        LocalDateTime startDate = LocalDateTime.of(2023, 5, 14, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 5, 14, 12, 0);
        planning = Planning.builder()
                .idPlanning(1)
                .dateStart(startDate)
                .dateEnd(endDate)
                .build();

        room = Room.builder()
                .numberRoom(1)
                .name("Tesla")
                .build();

        consulting1 = Consulting.builder()
                .idConsulting(1)
                .idTeam(1)
                .review("consulting")
                .specialityConsulting("DEV")
                .planning(planning)
                .room(room)
                .teacher(teacher1)
                .build();

        consulting2 = Consulting.builder()
                .idConsulting(2)
                .idTeam(2)
                .review("New Consulting")
                .specialityConsulting("INF")
                .planning(planning)
                .room(room)
                .teacher(teacher2)
                .build();

        Set<Consulting> listConsulting = new HashSet<>();
        listConsulting.add(consulting1);
        listConsulting.add(consulting2);
        planning.setConsultings(listConsulting);

        List<Consulting> consultings = new ArrayList<>();
        consultings.add(consulting1);
        consultings.add(consulting2);
        when(consultingRepository.findAll()).thenReturn(consultings);

        teacherService.getConsultings();

        verify(consultingRepository, times(1)).findAll();
    }

    @Test
    void testPostAddDisponibility() {
        when(teacherRepository.getReferenceById(9)).thenReturn(teacher1);
        teacher1.setPlannings(new HashSet<>());
        when(planningRepository.getReferenceById(1)).thenReturn(planning);

        PlanningRequest planningRequest = new PlanningRequest(1);
        teacherService.addDisponibility(planningRequest, 9);

        assertTrue(teacher1.getPlannings().contains(planning));
        verify(teacherRepository, times(1)).getReferenceById(9);
        verify(planningRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).save(teacher1);
    }

    @Test
    void testPostRemoveDisponibility() {
        when(teacherRepository.getReferenceById(9)).thenReturn(teacher1);
        teacher1.setPlannings(new HashSet<>());
        when(planningRepository.getReferenceById(1)).thenReturn(planning);
        PlanningRequest planningRequest = new PlanningRequest(1);
        teacher1.getPlannings().add(planning);
        teacherService.removeDisponibility(planningRequest, 9);

        assertFalse(teacher1.getPlannings().contains(planning));
        verify(teacherRepository, times(1)).getReferenceById(9);
        verify(planningRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).save(teacher1);
    }

    @Test
    void testValidateSpecialities() {
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher);
        StatusRequest statusRequest = new StatusRequest(true, 1,"");
        teacherService.validateSpecialities(statusRequest);
        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(teacherRepository, times(1)).save(teacher);
    }
    
    @Test
    void setPresentationCommentTeacher_shouldModifyTeacherComment() {
        LocalDateTime date = LocalDateTime.of(2024, 5, 14, 12, 0);

        Presentation presentation1 = Presentation.builder()
                .idPresentation(1)
                .type("INF")
                .dateHours(date)
                .commentTeam1("commentTeam1")
                .commentTeacher1("commentTeacher1")
                .commentTeam2("commentTeam2")
                .commentTeacher2("commentTeacher2")
                .build();
        when(presentationRepository.getReferenceById(presentation1.getIdPresentation())).thenReturn(presentation1);

        PresentationRequest presentationRequest1 = PresentationRequest.builder()
                .idPresentation(1)
                .commentTeacher1("newCommentTeacher1")
                .build();
        teacherService.setPresentationCommentTeacher(presentationRequest1);
        assertEquals("newCommentTeacher1", presentation1.getCommentTeacher1());
        assertEquals("commentTeacher2", presentation1.getCommentTeacher2());

        PresentationRequest presentationRequest2 = PresentationRequest.builder()
                .idPresentation(1)
                .commentTeacher2("newCommentTeacher2")
                .build();
        teacherService.setPresentationCommentTeacher(presentationRequest2);
        assertEquals("newCommentTeacher1", presentation1.getCommentTeacher1());
        assertEquals("newCommentTeacher2", presentation1.getCommentTeacher2());
        verify(presentationRepository, times(2)).getReferenceById(1);
    }

}