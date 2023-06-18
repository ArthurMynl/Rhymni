package com.projet_gl.rhymni.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.NameRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.ProjectStatusResponse;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.service.ProjectService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for project-related actions.
 */
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor

public class ProjectController {

    private final ProjectService projectService;

    /**
     * Updates a project description.
     *
     * @param descriptionRequest A {@link DescriptionRequest} object containing the
     *                           project ID and new description.
     */
    @PutMapping(value = "/updateDescription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDescription(@RequestBody DescriptionRequest descriptionRequest) {
        projectService.updateDescription(descriptionRequest);
    }

    /**
     * Updates a project description.
     *
     * @param descriptionRequest A {@link DescriptionRequest} object containing the
     *                           project ID and new description.
     */
    @PutMapping(value = "/updateName")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateName(@RequestBody NameRequest nameRequest) {
        projectService.updateName(nameRequest);
    }

    /**
     * Retrieves a list of all projects.
     *
     * @return A list of {@link ProjectResponse} objects.
     */
    @GetMapping(value = "/getProjects")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponse> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping(value = "/semester/getProjects")
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponse> getSemesterProjects() {
        return projectService.getSemesterProjects();
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param projectId The ID of the project to retrieve.
     * @return A {@link ProjectResponse} object containing the project's details.
     */
    @GetMapping(value = "/getProject/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProjectById(@PathVariable int projectId) {
        return projectService.getProject(projectId);
    }

    /**
     * Changes the status of a project.
     *
     * @param validateRequest A {@link StatusRequest} object containing the project
     *                        ID and new status.
     */
    @PutMapping(value = "/changeStatus")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateProject(@RequestBody StatusRequest validateRequest) {
        projectService.validateProject(validateRequest);
    }

    /**
     * Retrieves the status of a project by its ID.
     *
     * @param projectId The ID of the project to retrieve the status for.
     * @return A {@link ProjectStatusResponse} object containing the project's
     *         status.
     */
    @GetMapping(value = "/getProjectStatus/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectStatusResponse getProjectStatus(@PathVariable int projectId) {
        return projectService.getProjectStatus(projectId);
    }

    /**
     * Retrieves a project by its associated team ID.
     *
     * @param teamId The ID of the team to retrieve the project for.
     * @return A {@link ProjectResponse} object containing the project's details.
     */
    @GetMapping(value = "/getProjectByTeam/{teamId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProjectsByTeam(@PathVariable int teamId) {
        return projectService.getProjectByTeam(teamId);
    }

    @GetMapping(value = "/getValidationRights/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Integer> getValidationRights(@PathVariable int projectId) {
        return projectService.getValidationRights(projectId);
    }


}
