package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvValidationException;
import com.projet_gl.rhymni.dto.StudentRequest;
import com.projet_gl.rhymni.dto.StudentResponse;
import com.projet_gl.rhymni.service.StudentService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for student-related actions.
 */
@RestController
@RequestMapping("/student")
@RequiredArgsConstructor

public class StudentController {
	@Autowired
	StudentService studentService;

	/**
	 * Retrieves all students.
	 *
	 * @return A list of {@link StudentResponse} objects.
	 */
	@GetMapping(value = "/getStudents")
	@ResponseStatus(HttpStatus.OK)
	public List<StudentResponse> viewAllStudents() {
		return studentService.getAllStudents();
	}

	/**
	 * Retrieves a student by their ID.
	 *
	 * @param id The ID of the student to retrieve.
	 * @return A {@link StudentResponse} object containing the student's details.
	 */
	@GetMapping(value = "/getStudent/{id}")
	@ResponseStatus(HttpStatus.OK)
	public StudentResponse viewStudentById(@PathVariable Integer id) {
		return studentService.getStudentById(id);
	}

	/**
	 * Retrieves students who are in a team.
	 *
	 * @return A list of {@link StudentResponse} objects.
	 */
	@GetMapping(value = "/getStudentsInTeam")
	@ResponseStatus(HttpStatus.OK)
	public List<StudentResponse> viewTeamStudent() {
		return studentService.getStudentsTeam();
	}

	/**
	 * Retrieves students by the team ID they belong to.
	 *
	 * @param teamId The ID of the team to retrieve students for.
	 * @return A list of {@link StudentResponse} objects.
	 */
	@GetMapping(value = "/getStudentsInTeam/{teamId}")
	@ResponseStatus(HttpStatus.OK)
	public List<StudentResponse> viewTeamStudentById(@PathVariable Integer teamId) {
		return studentService.getStudentsTeam(teamId);
	}

	/**
	 * Imports students from a CSV file.
	 *
	 * @param multipartFile The CSV file to import students from.
	 * @return An {@link HttpStatus} object indicating the result of the import
	 *         operation.
	 * @throws CsvValidationException If the CSV file is not valid.
	 * @throws NumberFormatException  If there is an issue with the number format in
	 *                                the CSV file.
	 */
	@PostMapping(value = "/import")
	@ResponseStatus(HttpStatus.CREATED)
	public HttpStatus importStudent(@RequestParam("file") MultipartFile multipartFile)
			throws CsvValidationException, NumberFormatException {
		return studentService.importStudent(multipartFile);
	}

	@PutMapping(value = "/setStudentMarks")
	@ResponseStatus(HttpStatus.OK)
	public void setStudentMarks(@RequestBody StudentRequest studentRequest) {
		studentService.setStudentMarks(studentRequest);
	}
}
