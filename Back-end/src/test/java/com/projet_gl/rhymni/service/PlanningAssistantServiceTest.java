package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.opencsv.exceptions.CsvValidationException;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.PresentationResponse;
import com.projet_gl.rhymni.dto.RoomResponse;
import com.projet_gl.rhymni.dto.SemesterInfoRequest;
import com.projet_gl.rhymni.dto.SemesterInfoResponse;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Room;
import com.projet_gl.rhymni.entity.SemesterInfo;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.RoomRepository;
import com.projet_gl.rhymni.repository.SemesterInfoRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class PlanningAssistantServiceTest {

        @InjectMocks
        private PlanningAssistantService planningAssistantService;
        @Mock
        private PlanningRepository planningRepository;

        @Mock
        private RoomRepository roomRepository;

        @Mock
        private SemesterInfoRepository semesterRepository;

        @Mock
        private PresentationRepository presentationRepository;

        @Mock
        private TeamRepository teamRepository;

        @InjectMocks
        private Team team;

        @Mock
        private TeacherRepository teacherRepository;

        @InjectMocks
        private Teacher teacher;

        @Test
        void testHandleError_ReturnsBadRequest_ForProfPresentationError() {

                IllegalArgumentException e = new IllegalArgumentException("Le professeur a déjà une soutenance");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Le professeur a déjà une soutenance", response.getBody());
        }

        @Test
        void testHandleError_ReturnsConflict_ForTeamPresentationError() {

                IllegalArgumentException e = new IllegalArgumentException(
                                "L'équipe à déjà une Soutenance intermédiaire");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                assertEquals("L'équipe à déjà une Soutenance intermédiaire", response.getBody());
        }

        @Test
        void testHandleError_ReturnsBadGateway_ForMissingIntermediatePresentationError() {

                IllegalArgumentException e = new IllegalArgumentException(
                                "L'équipe n'à pas de soutenance intermédiaire");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
                assertEquals("L'équipe n'à pas de soutenance intermédiaire", response.getBody());
        }

        @Test
        void testHandleError_ReturnsExpectationFailed_ForInvalidPresentationOrderError() {

                IllegalArgumentException e = new IllegalArgumentException(
                                "Une soutenance finale doit être après la soutenance intermédiaire");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.EXPECTATION_FAILED, response.getStatusCode());
                assertEquals("Une soutenance finale doit être après la soutenance intermédiaire", response.getBody());
        }

        @Test
        void testHandleError_ReturnsConflict_ForFinalPresentationExistsError() {

                IllegalArgumentException e = new IllegalArgumentException(
                                "L'équipe à une soutenance finale, supprimez là avant de supprimer la soutenance intermédiaire");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
                assertEquals("L'équipe à une soutenance finale, supprimez là avant de supprimer la soutenance intermédiaire",
                                response.getBody());
        }

        @Test
        void testHandleError_ReturnsBadRequest_ForUnknownError() {

                IllegalArgumentException e = new IllegalArgumentException("Erreur inconnue");

                ResponseEntity<String> response = planningAssistantService.handleError(e);

                assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
                assertEquals("Erreur inconnue", response.getBody());
        }

        @Test
        void importConsultingTimeSlot_shouldImportTimeSlotsAndReturnCreatedStatus()
                        throws IOException, CsvValidationException {

                String csvData = "DateDebut;DateFin\n2023-05-12 09:00;2023-05-12 10:00\n2023-05-12 11:00;2023-05-12 12:00";
                InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
                MockMultipartFile csvFile = new MockMultipartFile("timeslots.csv", inputStream);

                HttpStatus status = planningAssistantService.importConsultingTimeSlot(csvFile);

                assertEquals(HttpStatus.CREATED, status);
                verify(planningRepository, times(2)).save(any(Planning.class));
        }

        @Test
        void importConsultingTimeSlot_shouldNotImportTimeSlotsAndReturnBadRequestStatusIfHeaderInvalid()
                        throws IOException, CsvValidationException {

                String csvData = "InvalidHeader;DateFin\n2023-05-12 09:00;2023-05-12 10:00";
                InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
                MockMultipartFile csvFile = new MockMultipartFile("timeslots.csv", inputStream);

                HttpStatus status = planningAssistantService.importConsultingTimeSlot(csvFile);

                assertEquals(HttpStatus.BAD_REQUEST, status);
                verify(planningRepository, never()).save(any(Planning.class));
        }

        @Test
        void importConsultingTimeSlot_shouldNotImportTimeSlotsAndReturnInternalServerErrorStatusIfIOExceptionOccurs()
                        throws IOException, CsvValidationException {

                MockMultipartFile csvFile = mock(MockMultipartFile.class);
                when(csvFile.getInputStream()).thenThrow(new IOException());

                HttpStatus status = planningAssistantService.importConsultingTimeSlot(csvFile);

                assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status);
                verify(planningRepository, never()).save(any(Planning.class));
        }

        @Test
        void testGetAllRooms() {

                Room room1 = Room.builder()
                                .numberRoom(1)
                                .name("TESLA")
                                .build();
                Room room2 = Room.builder()
                                .numberRoom(2)
                                .name("EDISON")
                                .build();
                List<Room> roomList = Arrays.asList(room1, room2);

                when(roomRepository.findAll()).thenReturn(roomList);

                List<RoomResponse> roomResponses = planningAssistantService.getAllRooms();

                assertEquals(2, roomResponses.size()); // Vérifie si le nombre de salles est correct

                RoomResponse roomResponse1 = roomResponses.get(0);
                assertEquals(1, roomResponse1.getNumberRoom()); // Vérifie si le numéro de salle est correct
                assertEquals("TESLA", roomResponse1.getName()); // Vérifie si le nom de salle est correct

                RoomResponse roomResponse2 = roomResponses.get(1);
                assertEquals(2, roomResponse2.getNumberRoom()); // Vérifie si le numéro de salle est correct
                assertEquals("EDISON", roomResponse2.getName());
        }

        @Test
        void testGetAllPresentations() {
                LocalDateTime date = LocalDateTime.of(2024, 5, 14, 12, 0);
                Speciality specialities = new Speciality(1, "Security");
                Set<Speciality> listSpecialities = new HashSet<>();
                Set<Teacher> listTeacher = new HashSet<>();
                Set<Team> listTeams = new HashSet<>();
                listSpecialities.add(specialities);

                Room room1 = Room.builder()
                                .numberRoom(1)
                                .name("TESLA")
                                .build();

                Teacher teacher = Teacher.builder()
                                .idUser(1)
                                .name("Louis")
                                .surname("Legendre")
                                .mailAddress("louis.legendre@example.com")
                                .specialities(listSpecialities)
                                .build();

                Team team1 = Team.builder()
                                .number(1)
                                .linkTeam(null)
                                .dateModifTestBook(date)
                                .project(null)
                                .students(null)
                                .linkToTestBook("my.eseo.fr")
                                .build();

                listTeams.add(team1);
                listTeacher.add(teacher);
                Presentation presentation1 = Presentation.builder()
                                .idPresentation(1)
                                .type("INF")
                                .dateHours(date)
                                .room(room1)
                                .teacher(listTeacher)
                                .teams(listTeams)
                                .commentTeam1("Bien")
                                .commentTeacher1("Très bien")
                                .commentTeam2("Bien")
                                .commentTeacher2("Très bien")
                                .build();

                List<Presentation> presentationList = Arrays.asList(presentation1);

                when(presentationRepository.findAll()).thenReturn(presentationList);

                List<PresentationResponse> presentationResponses = planningAssistantService.getAllPresentations();

                assertEquals(1, presentationResponses.size()); // Vérifie si le nombre de présentations est correct

                PresentationResponse presentationResponse1 = presentationResponses.get(0);
                assertEquals(1, presentationResponse1.getIdPresentation()); // Vérifie si l'id de présentation est
                                                                            // correct
                assertEquals("INF", presentationResponse1.getType()); // Vérifie si le type de présentation est correct
                assertEquals(date, presentationResponse1.getDateHours()); // Vérifie si la date/heure de présentation
                                                                          // est correcte
                assertEquals("Bien", presentationResponse1.getCommentTeam1()); // Vérifie si le commentaire de
                                                                               // présentation
                                                                               // est correct
                assertEquals("Très bien", presentationResponse1.getCommentTeacher1()); // Vérifie si le commentaire de
                                                                                       // présentation
                // est correct
                assertEquals("Bien", presentationResponse1.getCommentTeam2()); // Vérifie si le commentaire de
                                                                               // présentation
                // est correct
                assertEquals("Très bien", presentationResponse1.getCommentTeacher2()); // Vérifie si le commentaire de
                                                                                       // présentation
                // est correct
                assertEquals(listTeacher, presentationResponse1.getTeacher()); // Vérifie si le professeur de
                                                                               // présentation est correct
                assertEquals(listTeams, presentationResponse1.getTeams()); // Vérifie si les équipes de présentation
                                                                           // sont correctes
                assertEquals(room1, presentationResponse1.getRoom()); // Vérifie si la salle de présentation est
                                                                      // correcte
        }

        @Test
        void testIsTeamAvailable_WhenExistingPresentation_ReturnsFalse() {

                String type = "Type";
                Integer number = 1;
                Team team = new Team();
                team.setNumber(number);
                Presentation existingPresentation = new Presentation();
                when(teamRepository.getReferenceById(number)).thenReturn(team);
                when(presentationRepository.findByTypeAndTeams(type, team)).thenReturn(existingPresentation);

                boolean isAvailable = planningAssistantService.isTeamAvailable(type, number);

                assertFalse(isAvailable);
        }

        @Test
        void testIsTeamAvailable_WhenNoExistingPresentation_ReturnsTrue() {

                String type = "Type";
                Integer number = 1;
                Team team = new Team();
                team.setNumber(number);
                when(teamRepository.getReferenceById(number)).thenReturn(team);
                when(presentationRepository.findByTypeAndTeams(type, team)).thenReturn(null);

                boolean isAvailable = planningAssistantService.isTeamAvailable(type, number);

                assertTrue(isAvailable);
        }

        @Test
        void testIsTeacherAvailable_WhenExistingPresentation_ReturnsFalse() {

                String date = "2023-01-01";
                String hours = "09:00";
                int idTeacher = 1;
                Teacher teacher = new Teacher();
                teacher.setIdUser(idTeacher);
                Presentation existingPresentation = new Presentation();
                when(teacherRepository.getReferenceById(idTeacher)).thenReturn(teacher);
                when(presentationRepository.findByDateHoursAndTeacher(
                                LocalDateTime.parse(date + "T" + hours), teacher)).thenReturn(existingPresentation);

                boolean isAvailable = planningAssistantService.isTeacherAvailable(date, hours, idTeacher);

                assertFalse(isAvailable);
        }

        @Test
        void testIsTeacherAvailable_WhenNoExistingPresentations_ReturnsTrue() {

                String date = "2023-01-01";
                String hours = "09:00";
                int idTeacher = 1;
                Teacher teacher = new Teacher();
                teacher.setIdUser(idTeacher);
                when(teacherRepository.getReferenceById(idTeacher)).thenReturn(teacher);
                when(presentationRepository.findByDateHoursAndTeacher(
                                LocalDateTime.parse(date + "T" + hours), teacher)).thenReturn(null);
                when(presentationRepository.findByDateHoursAndTeacher(
                                LocalDateTime.parse(date + "T" + hours), teacher.getLinkTeacher())).thenReturn(null);

                boolean isAvailable = planningAssistantService.isTeacherAvailable(date, hours, idTeacher);

                assertTrue(isAvailable);
        }

        @Test
        void testGetAllPlanning() {

                Planning planning1 = Planning.builder()
                                .idPlanning(1)
                                .dateStart(LocalDateTime.now())
                                .dateEnd(LocalDateTime.now().plusHours(1))
                                .teachers(new HashSet<>())
                                .build();

                Planning planning2 = Planning.builder()
                                .idPlanning(2)
                                .dateStart(LocalDateTime.now().plusDays(1))
                                .dateEnd(LocalDateTime.now().plusDays(1).plusHours(1))
                                .teachers(new HashSet<>())
                                .build();

                Set<Teacher> teachers = new HashSet<>();
                Teacher teacher1 = new Teacher();
                teacher1.setIdUser(1);
                Teacher teacher2 = new Teacher();
                teacher2.setIdUser(2);
                teachers.add(teacher1);
                teachers.add(teacher2);

                planning1.setTeachers(teachers);
                planning2.setTeachers(teachers);

                when(planningRepository.findAll()).thenReturn(Arrays.asList(planning1, planning2));

                List<PlanningResponse> response = planningAssistantService.getAllPlanning();

                assertEquals(2, response.size());

                PlanningResponse response1 = response.get(0);
                assertEquals(Integer.valueOf(1), response1.getIdPlanning());
                assertEquals(planning1.getDateStart(), response1.getDateStart());
                assertEquals(planning1.getDateEnd(), response1.getDateEnd());
                assertEquals(Arrays.asList(1, 2), response1.getTeachersID());

                PlanningResponse response2 = response.get(1);
                assertEquals(Integer.valueOf(2), response2.getIdPlanning());
                assertEquals(planning2.getDateStart(), response2.getDateStart());
                assertEquals(planning2.getDateEnd(), response2.getDateEnd());
                assertEquals(Arrays.asList(1, 2), response2.getTeachersID());

                verify(planningRepository).findAll();
        }

        @Test
        void testCheckAvailability_WhenTeamNotAvailable_ThrowsException() {

                LocalDateTime date = LocalDateTime.now();
                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .date("2023-01-01")
                                .hours("09:00")
                                .idTeacher(1)
                                .idTeam(1)
                                .build();

                Teacher teacher = new Teacher();
                teacher.setIdUser(1);
                when(teacherRepository.getReferenceById(1)).thenReturn(teacher);

                Team team = new Team();
                team.setNumber(1);
                when(teamRepository.getReferenceById(1)).thenReturn(team);

                Presentation existingPresentation = new Presentation();
                when(presentationRepository.findByTypeAndTeams("Présentation Intermédiaire", team))
                                .thenReturn(existingPresentation);

                assertThrows(IllegalArgumentException.class, () -> {
                        planningAssistantService.checkAvailability(presentationRequest, "Présentation Intermédiaire");
                });

                verify(presentationRepository, never()).findByDateHoursAndTeacher(date, teacher);
        }

        @Test
        void testCheckAvailability_WhenTeacherNotAvailable_ThrowsException() {

                LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 9, 0);
                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .date("2023-01-01")
                                .hours("09:00")
                                .idTeacher(1)
                                .idTeam(1)
                                .build();

                Presentation presentation = new Presentation();
                presentation.setIdPresentation(2);
                presentation.setDateHours(dateTime);
                Set<Presentation> listPresentations = new HashSet<>();
                listPresentations.add(presentation);
                Teacher teacher = new Teacher();
                teacher.setIdUser(1);
                teacher.setPresentations(listPresentations);
                when(teacherRepository.getReferenceById(1)).thenReturn(teacher);

                when(presentationRepository.findByDateHoursAndTeacher(dateTime, teacher)).thenReturn(presentation);
                Team team = new Team();
                team.setNumber(1);
                when(teamRepository.getReferenceById(1)).thenReturn(team);

                assertThrows(IllegalArgumentException.class, () -> {
                        planningAssistantService.checkAvailability(presentationRequest, "Présentation Intermédiaire");
                });

                verify(presentationRepository).findByDateHoursAndTeacher(dateTime, teacher);
        }

        @Test
        void testCreateIntermediairePresentation_SuccessfulCreation() {

                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .date("2023-01-01")
                                .hours("09:00")
                                .idTeacher(1)
                                .idTeam(1)
                                .build();

                Teacher teacher = new Teacher();
                teacher.setIdUser(1);
                when(teacherRepository.getReferenceById(1)).thenReturn(teacher);

                Team team = new Team();
                team.setNumber(1);
                when(teamRepository.getReferenceById(1)).thenReturn(team);

                Presentation existingPresentation = null;
                when(presentationRepository.findByTypeAndTeams("Soutenance intermédiaire", team))
                                .thenReturn(existingPresentation);

                ResponseEntity<String> response = planningAssistantService
                                .createIntermediairePresentation(presentationRequest);

                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals("Soutenance intermédiaire créée avec succès.", response.getBody());

                verify(presentationRepository, times(1)).save(any(Presentation.class));
        }

        @Test
        void testCreatePresentation() {

                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .date("2023-01-01")
                                .hours("09:00")
                                .idTeacher(1)
                                .idTeam(1)
                                .idRoom(1)
                                .build();

                Teacher teacher = new Teacher();
                teacher.setIdUser(1);

                Teacher teacher2 = new Teacher();
                teacher2.setIdUser(2);
                teacher2.setLinkTeacher(teacher);
                teacher.setLinkTeacher(teacher2);

                when(teacherRepository.getReferenceById(1)).thenReturn(teacher);

                Team team = mock(Team.class); // Crée un mock object pour Team
                team.setNumber(1);

                Team linkedTeam = mock(Team.class); // Crée un mock object pour le linked team
                linkedTeam.setNumber(2);

                team.setLinkTeam(linkedTeam);
                linkedTeam.setLinkTeam(team);

                when(teamRepository.getReferenceById(1)).thenReturn(team);
                when(team.getLinkTeam()).thenReturn(linkedTeam); // Mocking the linked team

                Room room = new Room();
                room.setNumberRoom(1);
                when(roomRepository.getReferenceById(1)).thenReturn(room);

                Presentation presentation = planningAssistantService.createPresentation(presentationRequest,
                                "Présentation intermédiaire");

                assertEquals("Présentation intermédiaire", presentation.getType());
                assertEquals(LocalDateTime.parse("2023-01-01T09:00"), presentation.getDateHours());

                Set<Teacher> expectedTeacherSet = new HashSet<>();
                expectedTeacherSet.add(teacher);
                expectedTeacherSet.add(teacher2);
                assertEquals(expectedTeacherSet, presentation.getTeacher());

                Set<Team> expectedTeamSet = new HashSet<>();
                expectedTeamSet.add(team);
                expectedTeamSet.add(linkedTeam);
                assertEquals(expectedTeamSet, presentation.getTeams());

                assertEquals(room, presentation.getRoom());
        }

        @Test
        void testGetAllPlanningForTeacher() {

                Teacher teacher = new Teacher();
                teacher.setIdUser(1);
                Set<Teacher> listTeachers = new HashSet<>();
                listTeachers.add(teacher);

                Planning planning1 = Planning.builder()
                                .idPlanning(1)
                                .dateStart(LocalDateTime.now())
                                .dateEnd(LocalDateTime.now().plusHours(1))
                                .teachers(listTeachers)
                                .build();

                Planning planning2 = Planning.builder()
                                .idPlanning(2)
                                .dateStart(LocalDateTime.now().plusDays(1))
                                .dateEnd(LocalDateTime.now().plusDays(1).plusHours(1))
                                .teachers(listTeachers)
                                .build();

                when(planningRepository.findAllByTeachersIdUser(teacher.getIdUser()))
                                .thenReturn(Arrays.asList(planning1, planning2));

                List<PlanningResponse> result = planningAssistantService.getAllPlanningForTeacher(teacher.getIdUser());

                assertEquals(2, result.size());
                PlanningResponse planningResponse = result.get(0);
                assertEquals(Integer.valueOf(1), planningResponse.getIdPlanning());
                assertNotNull(planningResponse.getDateStart());
                assertNotNull(planningResponse.getDateEnd());
                assertEquals(Arrays.asList(1), planningResponse.getTeachersID());
        }

        @Test
        void testGetAndValidateDateHours() {

                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .date("2023-05-20")
                                .hours("10:30")
                                .build();

                LocalDateTime result = planningAssistantService.getAndValidateDateHours(presentationRequest);

                assertEquals(LocalDateTime.of(2023, 5, 20, 10, 30), result);
        }

        @Test
        void testGetAndValidatePresentation() {
                // Arrange
                PresentationRequest presentationRequest = PresentationRequest.builder()
                                .idPresentation(1)
                                .date("2023-05-20")
                                .hours("10:30")
                                .idRoom(1)
                                .idTeacher(1)
                                .idTeam(1)
                                .build();

                Teacher teacher = new Teacher();
                teacher.setIdUser(1);
                when(teacherRepository.getReferenceById(1)).thenReturn(teacher);

                when(teacherRepository.getReferenceById(teacher.getIdUser())).thenReturn(teacher);

                Presentation presentation = new Presentation();
                when(presentationRepository.getReferenceById(1)).thenReturn(presentation);

                Room room = new Room();
                room.setNumberRoom(1);
                when(roomRepository.getReferenceById(1)).thenReturn(room);

                // Act
                Presentation result = planningAssistantService.getAndValidatePresentation(presentationRequest);

                // Assert
                assertEquals(presentation, result);
                assertEquals(room, result.getRoom());
                Set<Teacher> expectedTeachers = new HashSet<>();
                expectedTeachers.add(teacher);
                expectedTeachers.add(teacher.getLinkTeacher());
                assertEquals(expectedTeachers, result.getTeacher());
        }

        @Test
        void updateStartDateProject() {
                // Given
                SemesterInfoRequest semesterRequest = new SemesterInfoRequest();
                semesterRequest.setSemesterId(1);
                semesterRequest.setStartDate(LocalDate.of(2023, 1, 1));

                SemesterInfo semester = new SemesterInfo();
                semester.setIdSemesterInfo(1);
                // Set up any necessary attributes in the semester object

                when(semesterRepository.getReferenceById(semesterRequest.getSemesterId())).thenReturn(semester);

                // When
                planningAssistantService.updateStartDateProject(semesterRequest);

                // Then
                verify(semesterRepository, times(1)).save(semester);
                assertEquals(semesterRequest.getStartDate(), semester.getStartDate());
        }

        @Test
        void updateEndDateProject() {
                // Given
                SemesterInfoRequest semesterRequest = new SemesterInfoRequest();
                semesterRequest.setSemesterId(1);
                semesterRequest.setEndDate(LocalDate.of(2023, 6, 30));

                SemesterInfo semester = new SemesterInfo();
                semester.setIdSemesterInfo(1);
                // Set up any necessary attributes in the semester object

                when(semesterRepository.getReferenceById(semesterRequest.getSemesterId())).thenReturn(semester);

                // When
                planningAssistantService.updateEndDateProject(semesterRequest);

                // Then
                verify(semesterRepository, times(1)).save(semester);
                assertEquals(semesterRequest.getEndDate(), semester.getEndDate());
        }

        @Test
        void getSemesterActual_shouldReturnSemesterInfoResponse() {
                // Given
                SemesterInfo semester = new SemesterInfo();
                semester.setIdSemesterInfo(1);
                // Set up any necessary attributes in the semester object

                when(semesterRepository.getReferenceById(LocalDate.now().getYear())).thenReturn(semester);

                // When
                SemesterInfoResponse result = planningAssistantService.getSemesterActual();

                // Then
                assertEquals(semester.getIdSemesterInfo(), result.getIdSemesterInfo());
                // Add assertions for other attributes of the SemesterInfoResponse object
        }


}
