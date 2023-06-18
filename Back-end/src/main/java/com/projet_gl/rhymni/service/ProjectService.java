package com.projet_gl.rhymni.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.NameRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.ProjectStatusResponse;
import com.projet_gl.rhymni.dto.ProjectValidationRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.entity.Project;
import com.projet_gl.rhymni.entity.ProjectStatus;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ProjectRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TeacherRepository teacherRepository;
    private final TeamRepository teamRepository;

    public void updateDescription(DescriptionRequest descriptionRequest) {
        Project project = projectRepository.getReferenceById(descriptionRequest.getId());
        project.setDescription(descriptionRequest.getDescription());
        projectRepository.save(project);
    }

    public void updateName(NameRequest nameRequest) {
        Project project = projectRepository.getReferenceById(nameRequest.getId());
        project.setName(nameRequest.getName());
        projectRepository.save(project);
    }

    public HttpStatus validateProject(StatusRequest validateRequest) {
        Project project = projectRepository.getReferenceById(validateRequest.getId());
        if (validateRequest.isValidate()) {
            project.setStatus(ProjectStatus.VALIDATED);
            project.setRejectComment("");
        } else {
            project.setStatus(ProjectStatus.REFUSED);
            project.setRejectComment(validateRequest.getRejectComment());
        }
        projectRepository.save(project);
        return HttpStatus.OK;
    }

    public ProjectResponse getProject(int projectId) {
        Project project = projectRepository.getReferenceById(projectId);
        return mapToProjectResponse(project);
    }

    public List<ProjectResponse> getProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream().map(this::mapToProjectResponse).collect(Collectors.toList());
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getIdProject())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .rejectComment(project.getRejectComment())
                .startDate(project.getSemesterInfo().getStartDate())
                .idSemesterInfo(project.getSemesterInfo().getIdSemesterInfo())
                .build();
    }

    public void delegateValidationProject(ProjectValidationRequest projectValidationRequest)
            throws EntityNotFoundException {

        Project project = projectRepository.findById(projectValidationRequest.getIdProject())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Project not found why this ID: " + projectValidationRequest.getIdProject()));

        if (project.getStatus().equals(ProjectStatus.PENDING)) {
            Teacher teacher = teacherRepository.findById(projectValidationRequest.getIdTeacher())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Teacher not found why this ID: " + projectValidationRequest.getIdTeacher()));

            project.getTeachers().add(teacher);
            projectRepository.save(project);
        }
    }

    public ProjectStatusResponse getProjectStatus(int projectId) {
        Project project = projectRepository.getReferenceById(projectId);
        return mapToProjectStatusResponse(project);
    }

    private ProjectStatusResponse mapToProjectStatusResponse(Project project) {
        return ProjectStatusResponse.builder().idProject(project.getIdProject()).status(project.getStatus()).build();
    }

    public ProjectResponse getProjectByTeam(int id) throws EntityNotFoundException {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found why this ID: " + id));
        if (team.getProject() == null) {
            return null;
        }
        return ProjectResponse.builder()
                .id(team.getProject().getIdProject())
                .name(team.getProject().getName())
                .status(team.getProject().getStatus())
                .description(team.getProject().getDescription())
                .build();
    }

    public Set<Integer> getValidationRights(Integer projectId) {
        return projectRepository.findTeacherIdsByProjectId(projectId);
    }

    public List<ProjectResponse> getSemesterProjects() {
        List<ProjectResponse> projects = getProjects();
        List<ProjectResponse> projectsToRemove = new ArrayList<>();

        for (ProjectResponse projectResponse : projects) {
            if (projectResponse.getIdSemesterInfo() != LocalDate.now().getYear()) {
                projectsToRemove.add(projectResponse);
            }
        }
        projects.removeAll(projectsToRemove);

        return projects;
    }
}