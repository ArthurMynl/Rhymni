package com.projet_gl.rhymni.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.NotificationResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Notification;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.NotificationRepository;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ConsultingRepository consultingRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public List<NotificationResponse> getAllNotificationsForUser(int id) {
        List<NotificationResponse> notifications = new ArrayList<>();
        List<Notification> allNotifications = notificationRepository.findAll();
        for (Notification notification : allNotifications) {
            addNotificationIfRecipientIsTeacher(id, notifications, notification);
        }
        return notifications;
    }

    private void addNotificationIfRecipientIsTeacher(int id, List<NotificationResponse> notifications,
            Notification notification) {
        for (Teacher teacher : notification.getTeacherReceivers()) {
            if (teacher.getIdUser().equals(id)) {
                notifications.add(createNotificationResponse(notification));
            }
            for (Student student : notification.getStudentReceivers()) {
                if (student.getIdUser().equals(id)) {
                    if (notification.getConsulting() != null) {
                        NotificationResponse newNotificationResponse = new NotificationResponse(
                                notification.getIdNotification(),
                                notification.getPersonneSend(),
                                notification.getMessage(),
                                notification.getConsulting().getIdConsulting());
                        notifications.add(newNotificationResponse);
                    } else {
                        NotificationResponse newNotificationResponse = new NotificationResponse(
                                notification.getIdNotification(),
                                notification.getPersonneSend(),
                                notification.getMessage(),
                                null);
                        notifications.add(newNotificationResponse);
                    }
                }
            }
        }
    }

    private NotificationResponse createNotificationResponse(Notification notification) {
        Integer consultingId = notification.getConsulting() != null ? notification.getConsulting().getIdConsulting()
                : null;
        return new NotificationResponse(
                notification.getIdNotification(),
                notification.getPersonneSend(),
                notification.getMessage(),
                consultingId);
    }

    public void addNotification(NotificationRequest notificationRequest) {
        notificationRepository.save(mapToNotification(notificationRequest));
    }

    public void acceptConsulting(NotificationRequest notificationRequest, int idTeacher) {
        Notification notification = notificationRepository.getReferenceById(notificationRequest.getIdNotification());

        Teacher teacher = teacherRepository.getReferenceById(idTeacher);
        Student student = studentRepository.getReferenceById(notification.getPersonneSend());
        Set<Student> students = new HashSet<>(studentRepository.findByTeamNumber(student.getTeam().getNumber()));

        notification.getConsulting().setTeacher(teacher);

        consultingRepository.save(notification.getConsulting());

        notification.setTeacherReceivers(null);

        Notification acceptNotification = Notification.builder()
                .idNotification(notificationRepository.findAll().size())
                .consulting(notification.getConsulting())
                .personneSend(idTeacher)
                .message("Consulting accepté par " + teacher.getSurname())
                .studentReceivers(students)
                .build();

        notificationRepository.save(acceptNotification);
    }

    public void refuseConsulting(NotificationRequest notificationRequest, int idTeacher) {
        Notification notification = notificationRepository.getReferenceById(notificationRequest.getIdNotification());

        Teacher teacher = teacherRepository.getReferenceById(idTeacher);
        Student student = studentRepository.getReferenceById(notification.getPersonneSend());
        Set<Student> students = new HashSet<>(studentRepository.findByTeamNumber(student.getTeam().getNumber()));

        if (notification.getTeacherReceivers().size() > 1) {
            notification.getTeacherReceivers().remove(teacher);
            notificationRepository.save(notification);
        } else {
            notification.setTeacherReceivers(null);
            notificationRepository.save(notification);

            Notification annulNotification = Notification.builder()
                    .idNotification(notificationRepository.findAll().size())
                    .consulting(null)
                    .personneSend(idTeacher)
                    .message("Consulting annulé, veuillez faire une nouvelle demande de consulting.")
                    .studentReceivers(students)
                    .build();

            notificationRepository.save(annulNotification);
        }
    }

    protected Notification mapToNotification(NotificationRequest notificationRequest) {

        Set<Teacher> teachers = new HashSet<>();
        for (Teacher teacher : teacherRepository.findAll()) {
            for (Speciality speciality : teacher.getSpecialities()) {
                if (speciality.getNameSpeciality().equals(notificationRequest.getSpeciality())) {
                    teachers.add(teacherRepository.getReferenceById(teacher.getIdUser()));
                }
            }
        }

        Consulting consulting = new Consulting(); // consulting correspondant au au idPlanning
        for (Consulting consult : consultingRepository.findAll()) {
            if (consult.getPlanning().getIdPlanning().equals(notificationRequest.getIdPlanning())) {
                consulting = consult;
            }
        }

        return Notification.builder()
                .idNotification(notificationRepository.findAll().size())
                .consulting(consulting)
                .personneSend(notificationRequest.getPersonneSend())
                .message(notificationRequest.getMessage())
                .teacherReceivers(teachers)
                .build();

    }

    public Set<Teacher> searchTeachers(int id) {
        Notification notif = notificationRepository.getReferenceById(id);
        return notif.getTeacherReceivers();
    }

    public Set<Student> searchStudents(int id) {
        return notificationRepository.getReferenceById(id).getStudentReceivers();
    }

}
