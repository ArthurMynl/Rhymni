package com.projet_gl.rhymni.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.dto.ConsultingRequest;
import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.SpecialityResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Notification;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.NotificationRepository;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.SpecialityRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultingService {

    private final ConsultingRepository consultingRepository;

    private final TeamRepository teamRepository;

    private final TeacherRepository teacherRepository;

    private final SpecialityRepository specialityRepository;

    private final PlanningRepository planningRepository;

    private final NotificationRepository notificationRepository;

    public List<ConsultingResponse> getAllConsultings() {
        List<ConsultingResponse> listResponse = new ArrayList<>();
        for (Consulting consulting : consultingRepository.findAll())
            listResponse.add(consultingToConsultingResponse(consulting));
        return listResponse;
    }

    public List<SpecialityResponse> getSpecialities() {
        List<SpecialityResponse> listResponse = new ArrayList<>();
        for (Speciality spec : specialityRepository.findAll())
            listResponse.add(specToSpecResponse(spec));
        return listResponse;
    }

    private SpecialityResponse specToSpecResponse(Speciality spec) {
        return SpecialityResponse.builder()
                .specialityId(spec.getIdSpeciality())
                .name(spec.getNameSpeciality())
                .build();
    }

    public void assignTeacherToConsulting(ConsultingRequest consultingRequest) {
        Consulting consulting = consultingRepository.getReferenceById(consultingRequest.getIdConsulting());
        consulting.setTeacher(teacherRepository.getReferenceById(consultingRequest.getIdTeacher()));
        consultingRepository.save(consulting);

        // Delete notification link with teacher
        for (Notification notif : notificationRepository.findAll()) {
            if (notif.getConsulting() != null
                    && notif.getConsulting().getIdConsulting().equals(consulting.getIdConsulting())) {
                notif.setTeacherReceivers(null);
                notificationRepository.save(notif);
            }
        }
    }

    private ConsultingResponse consultingToConsultingResponse(Consulting consulting) {
        ConsultingResponse respBuilder = ConsultingResponse.builder()
                .idConsulting(consulting.getIdConsulting())
                .idTeam(consulting.getIdTeam())
                .idPlanning(consulting.getPlanning().getIdPlanning())
                .speciality(consulting.getSpecialityConsulting())
                .review(consulting.getReview())
                .build();
        if (consulting.getTeacher() != null)
            respBuilder.setIdTeacher(consulting.getTeacher().getIdUser());
        else
            respBuilder.setIdTeacher(-1); // Value -1 because it returns 0 if it's null
        if (consulting.getReview() != null)
            respBuilder.setReview(consulting.getReview());
        return respBuilder;
    }

    public void createConsulting(ConsultingRequest consultingRequest) {
        Team team = teamRepository.getReferenceById(consultingRequest.getIdTeam());
        Planning slot = planningRepository.getReferenceById(consultingRequest.getIdPlanning());
        // Date format in LocalDateTime : YYYY-MM-DDThh:mm:ss
        Consulting consulting = Consulting.builder()
                .planning(planningRepository.getReferenceById(slot.getIdPlanning()))
                .idTeam(team.getNumber())
                .specialityConsulting(consultingRequest.getSpeciality())
                .build();
        consultingRepository.save(consulting);
    }

    public List<ConsultingResponse> getAllConsultingsForTeacher(Integer idTeacher) {
        List<Consulting> listConsulting = consultingRepository.findAllByTeacherIdUser(idTeacher);
        List<ConsultingResponse> listResponse = new ArrayList<>();
        for (Consulting consulting : listConsulting)
            listResponse.add(consultingToConsultingResponse(consulting));
        return listResponse;
    }

    public List<PlanningResponse> getAllAvailableSlots() {
        List<PlanningResponse> listResponse = new ArrayList<>();
        for (Planning planning : planningRepository.findAll()) {
            if (!planning.getTeachers().isEmpty())
                listResponse.add(planningToPlanningResponse(planning));
        }
        return listResponse;
    }

    private PlanningResponse planningToPlanningResponse(Planning planning) {
        List<Integer> teachersID = new ArrayList<>();
        for (Teacher teacher : planning.getTeachers())
            teachersID.add(teacher.getIdUser());
        return PlanningResponse.builder()
                .idPlanning(planning.getIdPlanning())
                .dateStart(planning.getDateStart())
                .dateEnd(planning.getDateEnd())
                .teachersID(teachersID)
                .build();
    }

}
