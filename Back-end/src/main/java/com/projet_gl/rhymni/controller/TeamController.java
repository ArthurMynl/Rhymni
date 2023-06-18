package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.LinkedTeamResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.service.TeamService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for team-related actions.
 */
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor

public class TeamController {

	private final TeamService teamService;

	/**
	 * Retrieves all teams.
	 *
	 * @return A list of {@link TeamResponse} objects.
	 */
	@GetMapping(value = "/getTeams")
	@ResponseStatus(HttpStatus.OK)
	public List<TeamResponse> viewAllTeams() {
		return teamService.getAllTeams();
	}

	/**
	 * Retrieves a linked team by its ID.
	 *
	 * @param id The ID of the team to retrieve the linked team for.
	 * @return A {@link LinkedTeamResponse} object containing the linked team's
	 *         details.
	 */
	@GetMapping(value = "/getLinkedTeam/{id}")
	@ResponseStatus(HttpStatus.OK)
	public LinkedTeamResponse getLinkedTeam(@PathVariable int id) {
		return teamService.getLinkedTeam(id);
	}

	/**
	 * Retrieves a team by the project ID it is associated with.
	 *
	 * @param id The ID of the project to retrieve the team for.
	 * @return A {@link TeamResponse} object containing the team's details.
	 */
	@GetMapping(value = "getTeamByProject/{id}")
	@ResponseStatus(HttpStatus.OK)
	public TeamResponse getTeamByProject(@PathVariable int id) {
		return teamService.getTeamByProject(id);
	}

	/**
	 * Retrieves unpaired teams.
	 *
	 * @return A list of {@link TeamResponse} objects representing unpaired teams.
	 */
	@GetMapping(value = "/getUnpairedTeams")
	@ResponseStatus(HttpStatus.OK)
	public List<TeamResponse> getFreeTeams() {
		return teamService.getUnpairedTeams();
	}

	@PutMapping(value = "/setPresentationCommentTeam")
	@ResponseStatus(HttpStatus.OK)
	public void setcommentTeam(@RequestBody PresentationRequest presentationRequest) {
		teamService.setcommentTeam(presentationRequest);
	}

	@GetMapping(value = "getTeamByNumber/{number}")
	@ResponseStatus(HttpStatus.OK)
	public TeamResponse getTeamByNumber(@PathVariable int number) {
		return teamService.getTeamByNumber(number);
	}

	@PutMapping(value = "/setMarkTeam")
	@ResponseStatus(HttpStatus.OK)
	public void setMarkTeam(@RequestBody TeamRequest teamRequest) {
		teamService.setMarkTeam(teamRequest);
	}
}
