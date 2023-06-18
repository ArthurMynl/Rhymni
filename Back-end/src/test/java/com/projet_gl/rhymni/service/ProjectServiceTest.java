package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.NameRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.ProjectValidationRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.entity.Project;
import com.projet_gl.rhymni.entity.ProjectStatus;
import com.projet_gl.rhymni.entity.SemesterInfo;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ProjectRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private Teacher teacher;
    private Team team;
    private SemesterInfo semester;

    @BeforeEach
    public void setUp() {
        semester = SemesterInfo.builder()
                    .idSemesterInfo(2023)      
                    .startDate(LocalDate.now().minusDays(4))
                    .endDate(LocalDate.now().plusDays(4))      
                    .build();
        project = new Project(1, "Project 1", "Description 1", ProjectStatus.PENDING, null, new HashSet<>(),"", semester);
        teacher = new Teacher(1, "Louis", "Legendre", "louis.legendre@example.com", "network", new HashSet<>());
        team = new Team();
        team.setNumber(1);
        team.setProject(project);
    }

    @Test
    void testUpdateDescription() {
        when(projectRepository.getReferenceById(1)).thenReturn(project);

        DescriptionRequest descriptionRequest = new DescriptionRequest("New description", 1);

        projectService.updateDescription(descriptionRequest);

        verify(projectRepository, times(1)).getReferenceById(1);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testValidateProject() {
        when(projectRepository.getReferenceById(1)).thenReturn(project);

        StatusRequest statusRequest = new StatusRequest(true, 1,"");

        projectService.validateProject(statusRequest);

        verify(projectRepository, times(1)).getReferenceById(1);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testGetProject() {
        when(projectRepository.getReferenceById(1)).thenReturn(project);

        projectService.getProject(1);

        verify(projectRepository, times(1)).getReferenceById(1);
    }

    @Test
    void testGetProjects() {
        List<Project> projectList = Arrays.asList(project);

        when(projectRepository.findAll()).thenReturn(projectList);

        projectService.getProjects();

        verify(projectRepository, times(1)).findAll();
    }

   @Test
    void testDelegateValidationProject() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));

        ProjectValidationRequest projectValidationRequest = new ProjectValidationRequest(1,1);
        projectService.delegateValidationProject(projectValidationRequest);

        verify(projectRepository, times(1)).findById(1);
        verify(teacherRepository, times(1)).findById(1);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testGetProjectStatus() {
        when(projectRepository.getReferenceById(1)).thenReturn(project);

        projectService.getProjectStatus(1);

        verify(projectRepository, times(1)).getReferenceById(1);
    }

    @Test
    void testGetProjectByTeam() {
    when(teamRepository.findById(1)).thenReturn(Optional.of(team));

    ProjectResponse projectResponse = projectService.getProjectByTeam(1);

    assertEquals(project.getIdProject(), projectResponse.getId());
    assertEquals(project.getName(), projectResponse.getName());
    assertEquals(project.getDescription(), projectResponse.getDescription());
    assertEquals(project.getStatus(), projectResponse.getStatus());

    verify(teamRepository, times(1)).findById(1);
    }

    @Test
    void updateName_shouldUpdateProjectName() {
        // Arrange
        int projectId = 1;
        String newName = "New Project Name";
        NameRequest nameRequest = new NameRequest();
        nameRequest.setId(projectId);
        nameRequest.setName(newName);

        Project project = new Project();
        project.setIdProject(projectId);
        project.setName("Old Project Name");

        when(projectRepository.getReferenceById(projectId)).thenReturn(project);

        // Act
        projectService.updateName(nameRequest);

        // Assert
        assertEquals(newName, project.getName());
        verify(projectRepository, times(1)).save(project);
        // Add additional assertions or verifications as needed
    }
}
