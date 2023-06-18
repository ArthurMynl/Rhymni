package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.ProjectValidationRequest;
import com.projet_gl.rhymni.dto.PairTeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.service.ProjectService;
import com.projet_gl.rhymni.service.TeamService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for option leader-related actions.
 */
@RestController
@RequestMapping("/optionLeader")
@RequiredArgsConstructor
public class OptionLeaderController {

    private final TeamService teamService;
    private final ProjectService projectService;

    /**
     * Creates a pair of teams.
     *
     * @param pairTeamRequest A {@link PairTeamRequest} object containing the team
     *                        IDs
     *                        to be
     *                        paired.
     */
    @PostMapping(value = "/createTeamPair")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTeamPair(@RequestBody PairTeamRequest pairTeamRequest) {
        teamService.createTeamPair(pairTeamRequest);
    }

    /**
     * Deletes a pair of teams.
     *
     * @param pairTeamRequest A {@link PairTeamRequest} object containing the team
     *                        IDs
     *                        to be
     *                        unpaired.
     */
    @PostMapping(value = "/deleteTeamPair")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeamPair(@RequestBody PairTeamRequest pairTeamRequest) {
        teamService.deleteTeamPair(pairTeamRequest);
    }

    /**
     * Delegates project validation to a teacher.
     *
     * @param projectValidationRequest A {@link ProjectValidationRequest} object
     *                                 containing the project ID and teacher ID.
     */
    @PutMapping(value = "/delegateValidationProject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delegateValidationProject(@RequestBody ProjectValidationRequest projectValidationRequest) {
        projectService.delegateValidationProject(projectValidationRequest);
    }

    /**
     * Retrieves all team pairs.
     *
     * @return A list of lists of {@link TeamResponse} objects, where each inner
     *         list represents a team pair.
     */
    @GetMapping("/getTeamPair")
    @ResponseStatus(HttpStatus.OK)
    public List<List<TeamResponse>> getTeamPair() {
        return teamService.getTeamPair();
    }

    /**
     * Retrieves all teams.
     *
     * @return A list of {@link TeamResponse} objects.
     */
    @GetMapping("/getAllTeams")
    @ResponseStatus(HttpStatus.OK)
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }

}
