package com.projet_gl.rhymni.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.NotificationResponse;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping(value = "/addNotification")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNotification(@RequestBody NotificationRequest notificationRequest) {
        notificationService.addNotification(notificationRequest);
    }

    /**
     * Get all notifications registered for a teacher
     */
    @GetMapping(value = "/getAllNotificationsForUser/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationResponse> getAllNotificationsForUser(@PathVariable int id) {
        return notificationService.getAllNotificationsForUser(id);
    }

    /**
     * Get all teachers registered for a notification
     * 
     * @return
     */
    @GetMapping(value = "/getListTeachersForNotification/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Teacher> getListTeachersForNotification(@PathVariable("id") int id) {
        return notificationService.searchTeachers(id);
    }

    /**
     * Get all students registered for a notification
     * 
     * @return
     */
    @GetMapping(value = "/getListStudentsForNotification/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Set<Student> getListStudentsForNotification(@PathVariable("id") int id) {
        return notificationService.searchStudents(id);
    }

}
