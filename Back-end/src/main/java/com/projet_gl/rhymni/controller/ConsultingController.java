package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.ConsultingRequest;
import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.SpecialityResponse;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.ConsultingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consulting")
@RequiredArgsConstructor
public class ConsultingController {

    private final ConsultingService consultingService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * Ask for a consulting
     * 
     * @param consultingRequest : A {@link ConsultingRequest} object containing the
     *                          team ID, the date and the hours
     */
    @PutMapping(value = "/askConsulting")
    @ResponseStatus(HttpStatus.CREATED)
    public void askConsulting(@RequestBody ConsultingRequest consultingRequest) {
        consultingService.createConsulting(consultingRequest);
    }

    /**
     * Get all the speciality registered in the DB
     * 
     * @return all the speciality registered in the DB
     */
    @GetMapping(value = "/getSpecialities")
    @ResponseStatus(HttpStatus.OK)
    public List<SpecialityResponse> getSpecialities() {
        return consultingService.getSpecialities();
    }

    /**
     * Get the list of all the consulting registered, from all teams
     * 
     * @return List of the {@link ConsultingResponse} objects registered
     */
    @GetMapping(value = "/getAllPlanned")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsultingResponse> getAllConsultings() {
        return consultingService.getAllConsultings();
    }

    /**
     * Assign a teacher to a consulting
     * 
     * @param idTeacher
     * @return
     */
    @PutMapping(value = "/assignTeacher")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignTeacher(@RequestBody ConsultingRequest consultingRequest) {
        consultingService.assignTeacherToConsulting(consultingRequest);
    }

    /**
     * Get all consultings registered for a teacher
     */
    @GetMapping(value = "/getAllForTeacher/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsultingResponse> getAllConsultingsForTeacher(@PathVariable("id") Integer idTeacher) {
        return consultingService.getAllConsultingsForTeacher(idTeacher);
    }

    /**
     * Get all slot available for student to register a consulting
     */
    @GetMapping(value = "/getAllAvailableSlots")
    @ResponseStatus(HttpStatus.OK)
    public List<PlanningResponse> getAllAvailableSlots() {
        return consultingService.getAllAvailableSlots();
    }
}
