package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.projet_gl.rhymni.dto.LinkedTeamResponse;
import com.projet_gl.rhymni.dto.PairTeamRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.dto.TeamStudentRequest;
import com.projet_gl.rhymni.entity.Options;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Project;
import com.projet_gl.rhymni.entity.ProjectStatus;
import com.projet_gl.rhymni.entity.SemesterInfo;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.ProjectRepository;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PresentationRepository presentationRepository;

    @Mock
    private ConsultingRepository consultingRepository;

    private Team team1;
    private Team team2;
    private Project project;
    private Student student;
    private SemesterInfo semester;

    @BeforeEach
    public void setUp() {
        semester = SemesterInfo.builder()
                .idSemesterInfo(2023)
                .startDate(LocalDate.now().minusDays(4))
                .endDate(LocalDate.now().plusDays(4))
                .build();
        project = new Project(1, "Project 1", "Description 1", ProjectStatus.PENDING, null, new HashSet<>(), "",
                semester);
        team1 = Team.builder()
                .number(1)
                .build();
        team2 = Team.builder()
                .number(2)
                .build();
        student = Student.builder()
                .idUser(1)
                .name("Louis")
                .surname("Legendre")
                .mailAddress("louis.legendre@example.com")
                .option(Options.LD)
                .team(null)
                .build();
    }

    @Test
    void getAllTeamsTest() {
        List<Team> teams = Arrays.asList(team1, team2);
        when(teamRepository.findAll()).thenReturn(teams);

        List<TeamResponse> result = teamService.getAllTeams();

        assertEquals(2, result.size());
        assertEquals(team1.getNumber(), result.get(0).getNumber());
        assertEquals(team2.getNumber(), result.get(1).getNumber());
    }

    @Test
    void registerStudentTest() {
        TeamStudentRequest teamStudentRequest = TeamStudentRequest.builder()
                                                    .idTeam(team1.getNumber())
                                                    .build();
        
        when(teamRepository.findById(team1.getNumber())).thenReturn(Optional.of(team1));
        when(studentRepository.findById(student.getIdUser())).thenReturn(Optional.of(student));

        teamService.registerStudent(teamStudentRequest, student.getIdUser());

        assertTrue(team1.getStudents().contains(student));
        verify(teamRepository, times(1)).save(team1);
    }

    @Test
    void getUnpairedTeamsTest() {
        team1.setLinkTeam(null);
        team2.setLinkTeam(team1);
        List<Team> teams = Arrays.asList(team1, team2);
        when(teamRepository.findAll()).thenReturn(teams);

        List<TeamResponse> result = teamService.getUnpairedTeams();

        assertEquals(1, result.size());
        assertEquals(team1.getNumber(), result.get(0).getNumber());
    }

    @Test
    void createTeamPairTest() {
        PairTeamRequest pairTeamRequest = new PairTeamRequest();
        pairTeamRequest.setIdLinkedTeam1(team1.getNumber());
        pairTeamRequest.setIdLinkedTeam2(team2.getNumber());

        when(teamRepository.getReferenceById(team1.getNumber())).thenReturn(team1);
        when(teamRepository.getReferenceById(team2.getNumber())).thenReturn(team2);

        teamService.createTeamPair(pairTeamRequest);

        assertEquals(team1.getLinkTeam(), team2);
        assertEquals(team2.getLinkTeam(), team1);
    }

    @Test
    void deleteTeamPairTest() {
        team1.setLinkTeam(team2);
        team2.setLinkTeam(team1);

        PairTeamRequest pairTeamRequest = new PairTeamRequest();
        pairTeamRequest.setIdLinkedTeam1(team1.getNumber());
        pairTeamRequest.setIdLinkedTeam2(team2.getNumber());

        when(teamRepository.getReferenceById(team1.getNumber())).thenReturn(team1);
        when(teamRepository.getReferenceById(team2.getNumber())).thenReturn(team2);

        teamService.deleteTeamPair(pairTeamRequest);

        assertNull(team1.getLinkTeam());
        assertNull(team2.getLinkTeam());
    }

    @Test
    void getTeamPairTest() {
        team1.setLinkTeam(team2);
        team2.setLinkTeam(team1);
        List<Team> teams = Arrays.asList(team1, team2);
        when(teamRepository.findAll()).thenReturn(teams);

        List<List<TeamResponse>> result = teamService.getTeamPair();

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals(team1.getNumber(), result.get(0).get(0).getNumber());
        assertEquals(team2.getNumber(), result.get(0).get(1).getNumber());
    }

    @Test
    void getLinkedTeamTest() {
        team1.setLinkTeam(team2);
        when(teamRepository.findById(team1.getNumber())).thenReturn(Optional.of(team1));

        LinkedTeamResponse result = teamService.getLinkedTeam(team1.getNumber());

        assertEquals(team1.getNumber(), result.getIdTeam());
        assertEquals(team2.getNumber(), result.getIdLinkedTeam());
    }

    @Test
    void getTeamByProjectTest() {
        team1.setProject(project);
        when(projectRepository.findById(project.getIdProject())).thenReturn(Optional.of(project));
        when(teamRepository.findByProject(project)).thenReturn(team1);

        TeamResponse result = teamService.getTeamByProject(project.getIdProject());

        assertEquals(team1.getNumber(), result.getNumber());
        assertEquals(team1.getProject().getIdProject(), result.getIdProject());
    }

    @Test
    void modifyTestBookTest() {
        String newTestBookLink = "http://new.testbook.link";
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setTeamNumber(team1.getNumber());
        teamRequest.setLinkTestBook(newTestBookLink);

        when(teamRepository.findById(team1.getNumber())).thenReturn(Optional.of(team1));

        teamService.modifyTestBook(teamRequest);

        verify(teamRepository).findById(team1.getNumber());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void setPresentationCommentTeam_shouldModifyTeamComment() {
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
                .commentTeam1("newCommentTeam1")
                .build();
        teamService.setcommentTeam(presentationRequest1);
        assertEquals("newCommentTeam1", presentation1.getCommentTeam1());
        assertEquals("commentTeam2", presentation1.getCommentTeam2());

        PresentationRequest presentationRequest2 = PresentationRequest.builder()
                .idPresentation(1)
                .commentTeam2("newCommentTeam2")
                .build();
        teamService.setcommentTeam(presentationRequest2);
        assertEquals("newCommentTeam1", presentation1.getCommentTeam1());
        assertEquals("newCommentTeam2", presentation1.getCommentTeam2());
        verify(presentationRepository, times(2)).getReferenceById(1);
    }

    @Test
    void removeStudentTest() {
        TeamStudentRequest teamStudentRequest = TeamStudentRequest.builder()
                                                .idTeam(team1.getNumber())
                                                .build();

        Set<Student> students = new HashSet<>();
        students.add(student);
    
        Team team = Team.builder()
                        .number(teamStudentRequest.getIdTeam())
                        .students(students)
                        .build();

        student.setTeam(team);
        
        when(teamRepository.getReferenceById(teamStudentRequest.getIdTeam())).thenReturn(team);
        when(studentRepository.getReferenceById(student.getIdUser())).thenReturn(student);

        teamService.removeStudent(teamStudentRequest, student.getIdUser());
        // Vérifie si l'étudiant a été supprimé de l'équipe
        assertEquals(0, team.getStudents().size());
        // Vérifie si l'équipe simulée a été enregistrée avec la méthode save() de teamRepository
        verify(teamRepository, times(1)).save(team);
    }

    void testSetMarkTeam() {
        int teamNumber = 1;
        float markPresentation = 9.5f;
        float markValidation = 8.2f;
        TeamRequest teamRequest = TeamRequest.builder()
                .teamNumber(teamNumber)
                .markPresentation(markPresentation)
                .markValidation(markValidation)
                .build();
        Team team = new Team();
        team.setNumber(teamNumber);

        when(teamRepository.getReferenceById(teamNumber)).thenReturn(team);

        teamService.setMarkTeam(teamRequest);

        assertEquals(markPresentation, team.getMarkPresentation());
        assertEquals(markValidation, team.getMarkValidation());
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void testGetTeamByNumber() {
        int teamNumber = 1;
        Team team = new Team();
        team.setNumber(teamNumber);
        TeamResponse expectedResponse = TeamResponse.builder()
                .number(teamNumber)
                .build();

        when(teamRepository.getReferenceById(teamNumber)).thenReturn(team);

        TeamResponse actualResponse = teamService.getTeamByNumber(teamNumber);

        assertEquals(expectedResponse.getNumber(), actualResponse.getNumber());
        verify(teamRepository, times(1)).getReferenceById(teamNumber);
    }

}