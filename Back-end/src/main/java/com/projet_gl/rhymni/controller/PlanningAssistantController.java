package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.PresentationResponse;
import com.projet_gl.rhymni.dto.RoomResponse;
import com.projet_gl.rhymni.dto.SemesterInfoRequest;
import com.projet_gl.rhymni.dto.SemesterInfoResponse;
import com.projet_gl.rhymni.service.PlanningAssistantService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for student-related actions.
 */
@RestController
@RequestMapping("/planning")
@RequiredArgsConstructor
public class PlanningAssistantController {
	@Autowired
	PlanningAssistantService planningAssistantService;

	/**
	 * Retrieves all presentation.
	 *
	 * @return A list of {@link PresentationResponse} objects.
	 */
	@GetMapping(value = "/getPresentations")
	@ResponseStatus(HttpStatus.OK)
	public List<PresentationResponse> viewAllPresentation() {
		return planningAssistantService.getAllPresentations();
	}

	/**
	 * Retrieves all presentation.
	 *
	 * @return A list of {@link PresentationResponse} objects.
	 */
	@GetMapping(value = "/getRooms")
	@ResponseStatus(HttpStatus.OK)
	public List<RoomResponse> viewAllRoom() {
		return planningAssistantService.getAllRooms();
	}

	/**
	 * Create a new intermediate presentation
	 *
	 * 
	 */
	@PostMapping(value = "/createIntermediatePresentation")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createIntermediairePresentation(
			@RequestBody PresentationRequest presentationRequest) {
		return planningAssistantService.createIntermediairePresentation(presentationRequest);
	}

	/**
	 * Create a new final presentation
	 *
	 * 
	 */
	@PostMapping(value = "/createFinalPresentation")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> createFinalPresentation(@RequestBody PresentationRequest presentationRequest) {
		return planningAssistantService.createFinalPresentation(presentationRequest);
	}

	/**
	 * Delete an intermediate and a final presentation
	 * But can't change the name of the function due to an unknown error
	 * 
	 */
	@PostMapping(value = "/deleteIntermediatePresentation")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> deleteIntermediairePresentation(
			@RequestBody PresentationRequest presentationRequest) {
		return planningAssistantService.deletePresentation(presentationRequest);
	}

	@PostMapping(value = "/import")
	public ResponseEntity<HttpStatus> importConsultingTimeSlot(@RequestParam("file") MultipartFile multipartFile)
			throws CsvValidationException, NumberFormatException {
		return ResponseEntity.status(planningAssistantService.importConsultingTimeSlot(multipartFile)).build();
	}

	/**
	 * Gets all plannings.
	 *
	 * @return A {@link PlanningResponse} object containing the planning's details.
	 */
	@GetMapping(value = "/getPlanning")
	@ResponseStatus(HttpStatus.OK)
	public List<PlanningResponse> getPlanning() {
		return planningAssistantService.getAllPlanning();
	}

	/**
	 * Gets plannings registered for a teacher.
	 *
	 * @return A {@link PlanningResponse} object containing the planning's details.
	 */
	@GetMapping(value = "/getAllPlanningsTeacher/{id}")
	@ResponseStatus(HttpStatus.OK)
	public List<PlanningResponse> getAllPlanningForTeacher(@PathVariable("id") Integer idTeacher) {
		return planningAssistantService.getAllPlanningForTeacher(idTeacher);
	}

	/**
	 * Modify an intermediate presentation
	 *
	 * 
	 */
	@PostMapping(value = "/modifyIntermediatePresentation")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> modifyIntermediairePresentation(
			@RequestBody PresentationRequest presentationRequest) {
		return planningAssistantService.modifyIntermediairePresentation(presentationRequest);
	}

	/**
	 * Modify an intermediate presentation
	 *
	 * 
	 */
	@PostMapping(value = "/modifyFinalPresentation")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> modifyFinalPresentation(@RequestBody PresentationRequest presentationRequest) {
		return planningAssistantService.modifyFinalPresentation(presentationRequest);
	}

	@PutMapping(value = "/updateStartProject")
    @ResponseStatus(HttpStatus.OK)
    public void updateStartProject(@RequestBody SemesterInfoRequest semesterRequest) {
        planningAssistantService.updateStartDateProject(semesterRequest);
    }

	@PutMapping(value = "/updateEndProject")
    @ResponseStatus(HttpStatus.OK)
    public void updateEndProject(@RequestBody SemesterInfoRequest semesterRequest) {
        planningAssistantService.updateEndDateProject(semesterRequest);
    }

	@GetMapping(value = "/semester")
    @ResponseStatus(HttpStatus.OK)
    public SemesterInfoResponse getSemesterActual() {
        return planningAssistantService.getSemesterActual();
    }
}