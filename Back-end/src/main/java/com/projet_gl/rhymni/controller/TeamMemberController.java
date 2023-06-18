package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.DescriptionRequest;
import com.projet_gl.rhymni.dto.ProjectResponse;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamStudentRequest;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.security.AuthResponse;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.ProjectService;
import com.projet_gl.rhymni.service.TeamService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

/**
 * Rest controller for team member-related actions.
 */
@RestController
@RequestMapping("/teamMember")
@RequiredArgsConstructor

public class TeamMemberController {

	private final ProjectService projectService;
	private final TeamService teamService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	StudentRepository studentRepository;

	/**
	 * Updates the description of a project.
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
	 * Retrieves all projects.
	 *
	 * @return A list of {@link ProjectResponse} objects.
	 */
	@GetMapping(value = "/getProjects")
	@ResponseStatus(HttpStatus.OK)
	public List<ProjectResponse> getProjects() {
		return projectService.getProjects();
	}

	/**
	 * Registers a student to a team.
	 *
	 * @param teamStudentRequest A {@link TeamStudentRequest} object containing the
	 *                           student ID and team ID.
	 */
	@PutMapping(value = "/registerStudent")
	@ResponseStatus(HttpStatus.OK)
	public AuthResponse registerStudentTeam(@RequestBody TeamStudentRequest teamStudentRequest,
			@RequestHeader("Authorization") String token) {
		token = token.substring(7);
		Claims claims = jwtTokenUtil.parseClaims(token);
		int idStudent = (int) claims.get("id");
		teamService.registerStudent(teamStudentRequest, idStudent);
		Student user = studentRepository.getReferenceById(idStudent);
		String accessToken = jwtTokenUtil.generateAccessToken(user);
		return new AuthResponse(user.getMailAddress(), accessToken);
	}

	/**
	 * Remove a student to a team.
	 *
	 * @param teamStudentRequest A {@link TeamStudentRequest} object containing the
	 *                           student ID and team ID.
	 */
	@PutMapping(value = "/removeStudent")
	@ResponseStatus(HttpStatus.OK)
	public AuthResponse removeStudentTeam(@RequestBody TeamStudentRequest teamStudentRequest,
			@RequestHeader("Authorization") String token) {
		token = token.substring(7);
		Claims claims = jwtTokenUtil.parseClaims(token);
		int idStudent = (int) claims.get("id");
		teamService.removeStudent(teamStudentRequest, idStudent);
		Student user = studentRepository.getReferenceById(idStudent);
		String accessToken = jwtTokenUtil.generateAccessToken(user);
		return new AuthResponse(user.getMailAddress(), accessToken);
	}

	@PutMapping(value = "/modifyTestBook")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void modifyTestBook(@RequestBody TeamRequest teamRequest) {
		teamService.modifyTestBook(teamRequest);
	}
}
