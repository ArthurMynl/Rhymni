package com.projet_gl.rhymni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.PlanningRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.ReviewRequest;
import com.projet_gl.rhymni.dto.SpecialityRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.dto.TeacherRequest;
import com.projet_gl.rhymni.dto.TeacherResponse;
import com.projet_gl.rhymni.security.JwtTokenUtil;
import com.projet_gl.rhymni.service.NotificationService;
import com.projet_gl.rhymni.service.TeacherService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

/**
 * Rest controller for teacher-related actions.
 */
@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor

public class TeacherController {

    private final TeacherService teacherService;
    private final NotificationService notificationService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    /**
     * Gets a teacher by their ID.
     *
     * @param id The ID of the teacher to retrieve.
     * @return A {@link TeacherResponse} object containing the teacher's details.
     */
    @GetMapping(value = "/getTeacher/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TeacherResponse getTeacher(@PathVariable int id) {
        return teacherService.getTeacher(id);
    }

    /**
     * Adds a speciality to a teacher.
     *
     * @param specialityRequest A {@link SpecialityRequest} object containing the
     *                          teacher ID and speciality information.
     */
    @PostMapping(value = "/addSpeciality")
    @ResponseStatus(HttpStatus.CREATED)
    public void addSpeciality(@RequestBody SpecialityRequest specialityRequest) {
        teacherService.addSpeciality(specialityRequest);
    }

    /**
     * Removes a speciality from a teacher.
     *
     * @param specialityRequest A {@link SpecialityRequest} object containing the
     *                          teacher ID and speciality information.
     */
    @PostMapping(value = "/removeSpeciality")
    @ResponseStatus(HttpStatus.OK)
    public void removeSpeciality(@RequestBody SpecialityRequest specialityRequest) {
        teacherService.removeSpeciality(specialityRequest);
    }

    /**
     * Creates a pair of teachers.
     *
     * @param teacherRequest A {@link TeacherRequest} object containing the teacher
     *                       IDs.
     */
    @PutMapping(value = "/pairTeachers")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createPairOfTeacher(@RequestBody TeacherRequest teacherRequest) {
        teacherService.createPairOfTeacher(teacherRequest);
    }

    /**
     * Retrieves the list of all teachers.
     *
     * @return A list of {@link TeacherResponse} objects.
     */
    @GetMapping(value = "/getTeacher")
    @ResponseStatus(HttpStatus.OK)
    public List<TeacherResponse> viewTeacher() {
        return teacherService.getAllTeachers();
    }

    /**
     * Retrieves the list of paired teachers.
     *
     * @return A list of lists of {@link TeacherResponse} objects, where each inner
     *         list represents a pair of teachers.
     */
    @GetMapping(value = "/getPairTeacher")
    @ResponseStatus(HttpStatus.OK)
    public List<List<TeacherResponse>> viewPairTeacher() {
        return teacherService.getAllPairTeacher();
    }

    @PutMapping(value = "/updateReview")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReview(@RequestBody ReviewRequest reviewRequest) {
        teacherService.updateReview(reviewRequest);
    }

    @GetMapping(value = "/getConsultings")
    @ResponseStatus(HttpStatus.OK)
    public List<ConsultingResponse> viewConsulting() {
        return teacherService.getConsultings();
    }

    @GetMapping(value = "/getConsulting/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsultingResponse getProjectById(@PathVariable int id) {
        return teacherService.getConsulting(id);
    }

    /**
     * Adds a disponibility to a teacher.
     *
     * @param planningRequest A {@link PlanningRequest} object containing the
     *                        teacher ID and speciality information.
     */
    @PostMapping(value = "/addDisponibility")
    @ResponseStatus(HttpStatus.CREATED)
    public void addDisponibility(@RequestBody PlanningRequest planningRequest,
            @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        int idTeacher = (int) claims.get("id");
        teacherService.addDisponibility(planningRequest, idTeacher);
    }

    /**
     * Removes a disponibility from a teacher.
     *
     * @param specialityRequest A {@link PlanningRequest} object containing the
     *                          teacher ID and speciality information.
     */
    @PostMapping(value = "/removeDisponibility")
    @ResponseStatus(HttpStatus.OK)
    public void removeDisponibility(@RequestBody PlanningRequest planningRequest,
            @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        int idTeacher = (int) claims.get("id");
        teacherService.removeDisponibility(planningRequest, idTeacher);
    }

    @PutMapping(value = "/acceptNotification")
    @ResponseStatus(HttpStatus.CREATED)
    public void acceptNotification(@RequestBody NotificationRequest notificationRequest,
            @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        int idTeacher = (int) claims.get("id");
        notificationService.acceptConsulting(notificationRequest, idTeacher);
    }

    @PutMapping(value = "/refuseNotification")
    @ResponseStatus(HttpStatus.OK)
    public void refuseNotification(@RequestBody NotificationRequest notificationRequest,
            @RequestHeader("Authorization") String token) {
        token = token.substring(7);
        Claims claims = jwtTokenUtil.parseClaims(token);
        int idTeacher = (int) claims.get("id");
        notificationService.refuseConsulting(notificationRequest, idTeacher);
    }

    @PutMapping(value = "/setPresentationCommentTeacher")
    @ResponseStatus(HttpStatus.OK)
    public void setPresentationCommentTeacher(@RequestBody PresentationRequest presentationRequest) {
        teacherService.setPresentationCommentTeacher(presentationRequest);
    }

    @PutMapping(value = "/changeStatusSpecialities")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateSpecialities(@RequestBody StatusRequest validateRequest) {
        teacherService.validateSpecialities(validateRequest);
    }
}
