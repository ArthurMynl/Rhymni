package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projet_gl.rhymni.dto.NotificationRequest;
import com.projet_gl.rhymni.dto.NotificationResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Notification;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.NotificationRepository;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private TeacherService teacherService;
    @Mock
    private ConsultingRepository consultingRepository;

    private Notification notification1 = new Notification();
    private Teacher teacher1 = new Teacher();
    private Student student1 = new Student();
    private Team team1 = new Team();

    @Test
    void getAllNotificationsForUser_shouldReturnAllNotificationsForTeacher() {
        int teacherId = 1;
        notification1 = Notification.builder()
                .idNotification(1)
                .personneSend(1)
                .message("Message 1")
                .studentReceivers(new HashSet<Student>())
                .build();

        List<Notification> allNotifications = new ArrayList<Notification>();
        Set<Teacher> teacherReceivers1 = new HashSet<Teacher>();
        teacherReceivers1.add(new Teacher(1, "test", "test", null, null, null));

        notification1.setTeacherReceivers(teacherReceivers1);
        allNotifications.add(notification1);

        Notification notification2 = Notification.builder()
                .idNotification(2)
                .personneSend(2)
                .message("Message 2")
                .studentReceivers(new HashSet<Student>())
                .build();

        Set<Teacher> teacherReceivers2 = new HashSet<Teacher>();

        teacherReceivers2.add(new Teacher(2, "Ok", null, null, null, null));
        notification2.setTeacherReceivers(teacherReceivers2);

        allNotifications.add(notification2);

        when(notificationRepository.findAll()).thenReturn(allNotifications);

        List<NotificationResponse> responseList = notificationService.getAllNotificationsForUser(teacherId);

        assertEquals(1, responseList.size());
        assertEquals(1, responseList.get(0).getIdNotification());
        assertEquals(1, responseList.get(0).getPersonneSend());
        assertEquals("Message 1", responseList.get(0).getMessage());
    }

    @Test
    void addNotification_shouldSaveNotification() {
        // Arrange
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setMessage("New Message");
        notificationRequest.setPersonneSend(1);
        Speciality speciality = new Speciality(1, "MOD");
        notificationRequest.setSpeciality(speciality.nameSpeciality);

        notificationService.addNotification(notificationRequest);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void TestAcceptConsultingAndCreateNotification() {
        notification1 = Notification.builder()
                .consulting(new Consulting(1, null, null, null, null, teacher1, null))
                .personneSend(3)
                .build();

        teacher1 = Teacher.builder()
                .surname("test")
                .build();

        team1 = Team.builder()
                .number(1)
                .build();

        student1 = Student.builder()
                .team(team1)
                .build();

        List<Student> students = new ArrayList<Student>();
        students.add(student1);

        when(notificationRepository.getReferenceById(1)).thenReturn(notification1);
        when(teacherRepository.getReferenceById(9)).thenReturn(teacher1);
        when(studentRepository.getReferenceById(notification1.getPersonneSend())).thenReturn(student1);
        when(studentRepository.findByTeamNumber(student1.getTeam().getNumber())).thenReturn(students);

        NotificationRequest notificationRequest = new NotificationRequest(1, null, null, null, null);
        notificationService.acceptConsulting(notificationRequest, 9);

        assertEquals(teacher1, notification1.getConsulting().getTeacher());
        verify(consultingRepository, times(1)).save(notification1.getConsulting());
        assertNull(notification1.getTeacherReceivers());

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testRefuseConsulting_withManyTeacherReceive() {
        Set<Teacher> teachers = new HashSet<Teacher>();
        teachers.add(teacher1);
        teachers.add(new Teacher());
        teachers.add(new Teacher(11, null, null, null, null, null));

        notification1 = Notification.builder()
                .idNotification(1)
                .personneSend(3)
                .consulting(new Consulting(1, null, null, null, null, teacher1, null))
                .teacherReceivers(teachers)
                .build();

        student1 = Student.builder()
                .team(team1)
                .build();

        team1 = Team.builder()
                .number(1)
                .build();

        Student student2 = Student.builder()
                .team(team1)
                .build();

        List<Student> students = new ArrayList<Student>();
        students.add(student2);
        students.add(student1);

        when(notificationRepository.getReferenceById(1)).thenReturn(notification1);
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(studentRepository.getReferenceById(notification1.getPersonneSend())).thenReturn(student1);
        when(studentRepository.findByTeamNumber(student1.getTeam().getNumber())).thenReturn(students);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setIdNotification(1);
        notificationService.refuseConsulting(notificationRequest, 1);

        verify(notificationRepository, times(1)).save(notification1);

    }

    @Test
    void testRefuseConsulting_withOnlyOneTeacherReceive() {
        Set<Teacher> teachers = new HashSet<Teacher>();
        teachers.add(teacher1);
        notification1 = Notification.builder()
                .idNotification(1)
                .personneSend(3)
                .consulting(new Consulting(1, null, null, null, null, teacher1, null))
                .teacherReceivers(teachers)
                .build();

        team1 = Team.builder()
                .number(1)
                .build();

        student1 = Student.builder()
                .team(team1)
                .build();

        Student student2 = Student.builder()
                .team(team1)
                .build();

        List<Student> students = new ArrayList<Student>();
        students.add(student2);
        students.add(student1);
        Set<Student> studentsSet = new HashSet<Student>();
        studentsSet.add(student1);
        studentsSet.add(student2);

        when(notificationRepository.getReferenceById(1)).thenReturn(notification1);
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher1);
        when(studentRepository.getReferenceById(notification1.getPersonneSend())).thenReturn(student1);
        when(studentRepository.findByTeamNumber(student1.getTeam().getNumber())).thenReturn(students);

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setIdNotification(1);
        notificationService.refuseConsulting(notificationRequest, 1);

        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void testGetSearchTeachers() {
        when(notificationRepository.getReferenceById(1)).thenReturn(notification1);
        Set<Teacher> teacherReceivers = new HashSet<>();
        teacherReceivers.add(teacher1);
        notification1.setTeacherReceivers(teacherReceivers);

        Set<Teacher> result = notificationService.searchTeachers(1);

        assertEquals(teacherReceivers, result);
        verify(notificationRepository, times(1)).getReferenceById(1);
    }

    @Test
    void testGetSearchStudents() {
        when(notificationRepository.getReferenceById(1)).thenReturn(notification1);
        Set<Student> studentReceivers = new HashSet<>();
        studentReceivers.add(student1);
        notification1.setStudentReceivers(studentReceivers);

        Set<Student> result = notificationService.searchStudents(1);

        assertEquals(studentReceivers, result);
        verify(notificationRepository, times(1)).getReferenceById(1);
    }

    @Test
    void mapToNotification_shouldMapNotificationRequestToNotification() {
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .speciality("DEV")
                .idNotification(1)
                .personneSend(1)
                .message("Message test")
                .build();

        Speciality speciality = new Speciality();
        speciality.setNameSpeciality("DEV");
        Teacher teacher = new Teacher();
        Set<Speciality> specialities = new HashSet<Speciality>();
        specialities.add(speciality);
        teacher.setIdUser(1);
        teacher.setSpecialities(specialities);

        Planning planning = Planning.builder()
                .idPlanning(1)
                .build();
        Consulting consulting = Consulting.builder()
                .planning(planning)
                .build();

        List<Teacher> allTeachers = Collections.singletonList(teacher);

        when(teacherRepository.findAll()).thenReturn(allTeachers);
        when(teacherRepository.getReferenceById(1)).thenReturn(teacher);
        when(consultingRepository.findAll()).thenReturn(Collections.singletonList(consulting));
        when(notificationRepository.findAll()).thenReturn(Collections.emptyList());

        Notification result = notificationService.mapToNotification(notificationRequest);

        assertEquals(0, result.getIdNotification());
        assertEquals(1, result.getPersonneSend());
        assertEquals("Message test", result.getMessage());
        assertEquals(Collections.singleton(teacher), result.getTeacherReceivers());

        verify(teacherRepository, times(1)).findAll();
        verify(teacherRepository, times(1)).getReferenceById(1);
        verify(consultingRepository, times(1)).findAll();
        verify(notificationRepository, times(1)).findAll();
    }

}