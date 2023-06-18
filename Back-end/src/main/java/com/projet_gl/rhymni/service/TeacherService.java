package com.projet_gl.rhymni.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.dto.ConsultingResponse;
import com.projet_gl.rhymni.dto.PlanningRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.ReviewRequest;
import com.projet_gl.rhymni.dto.SpecialityRequest;
import com.projet_gl.rhymni.dto.StatusRequest;
import com.projet_gl.rhymni.dto.TeacherRequest;
import com.projet_gl.rhymni.dto.TeacherResponse;
import com.projet_gl.rhymni.entity.Consulting;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Role;
import com.projet_gl.rhymni.entity.SpecialitiesStatus;
import com.projet_gl.rhymni.entity.Speciality;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.repository.ConsultingRepository;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.SpecialityRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    protected final TeacherRepository teacherRepository;
    protected final SpecialityRepository specialityRepository;
    protected final ConsultingRepository consultingRepository;
    protected final PlanningRepository planningRepository;
    protected final PresentationRepository presentationRepository;

    public TeacherResponse getTeacher(int id) {
        Teacher teacher = teacherRepository.getReferenceById(id);
        return mapToTeacherResponse(teacher);
    }

    public void addSpeciality(SpecialityRequest specialityRequest) {
        Teacher teacher = teacherRepository.getReferenceById(specialityRequest.getIdTeacher());
        Set<Speciality> specialties = teacher.getSpecialities();
        specialties.add(specialityRepository.getReferenceById(specialityRequest.getSpecialityId()));
        teacher.setSpecialities(specialties);
        teacherRepository.save(teacher);
    }

    public void removeSpeciality(SpecialityRequest specialityRequest) {
        Teacher teacher = teacherRepository.getReferenceById(specialityRequest.getIdTeacher());
        Set<Speciality> specialties = teacher.getSpecialities();
        specialties.remove(specialityRepository.getReferenceById(specialityRequest.getSpecialityId()));
        teacher.setSpecialities(specialties);
        teacherRepository.save(teacher);
    }

    public void createPairOfTeacher(TeacherRequest teacherRequest) {
        Teacher teacher1 = teacherRepository.getReferenceById(teacherRequest.getIdTeacher1());
        Teacher teacher2 = teacherRepository.getReferenceById(teacherRequest.getIdTeacher2());
        Set<Speciality> mergedSpecialities = mergeSpecialities(teacher1, teacher2);
        Set<String> specialityNames = extractSpecialityNames(mergedSpecialities);

        if (areEligibleForPairing(teacher1, teacher2, specialityNames)) {
            unlinkExistingPairs(teacher1, teacher2);
            linkTeachers(teacher1, teacher2);
        } else {
            log.info("You need both of 'modélisation/developpment' and 'infrastructure' specialities");
        }
    }

    protected Set<Speciality> mergeSpecialities(Teacher teacher1, Teacher teacher2) {
        Set<Speciality> mergedSpecialities = new HashSet<>();
        mergedSpecialities.addAll(teacher1.getSpecialities());
        mergedSpecialities.addAll(teacher2.getSpecialities());
        return mergedSpecialities;
    }

    protected Set<String> extractSpecialityNames(Set<Speciality> specialities) {
        return specialities.stream().map(Speciality::getNameSpeciality).collect(Collectors.toSet());
    }

    protected boolean areEligibleForPairing(Teacher teacher1, Teacher teacher2, Set<String> specialityNames) {
        return !teacher1.equals(teacher2) &&
                (specialityNames.contains("MOD") || specialityNames.contains("DEV")) &&
                specialityNames.contains("INF");
    }

    protected void unlinkExistingPairs(Teacher teacher1, Teacher teacher2) {
        teacherRepository.findAll().stream()
                .filter(teacher -> teacher.getLinkTeacher() != null &&
                        (teacher.getLinkTeacher().equals(teacher1) || teacher.getLinkTeacher().equals(teacher2)))
                .forEach(teacher -> {
                    teacher.setLinkTeacher(null);
                    teacher.setRole(Role.ROLE_TEACHER);
                    teacherRepository.save(teacher);
                    log.info("Turned linkTeacher to null");
                });
    }

    protected void linkTeachers(Teacher teacher1, Teacher teacher2) {
        teacher1.setLinkTeacher(teacher2);
        teacher2.setLinkTeacher(teacher1);
        if (teacher1.getRole().equals(Role.ROLE_TEACHER))
            teacher1.setRole(Role.ROLE_JURY_MEMBER);
        if (teacher2.getRole().equals(Role.ROLE_TEACHER))
            teacher2.setRole(Role.ROLE_JURY_MEMBER);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        log.info("Linked successfully");
    }

    public List<TeacherResponse> getAllTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();

        return teachers.stream().map(this::mapToTeacherResponse).collect(Collectors.toList());
    }

    public List<List<TeacherResponse>> getAllPairTeacher() {
        List<Teacher> teachers = teacherRepository.findAll();
        List<List<TeacherResponse>> linkedTeachers = new ArrayList<>();

        for (Teacher teacher : teachers) {
            if (teacher.getLinkTeacher() != null) {
                Optional<Teacher> optionalLinkedTeacher = findLinkedTeacher(teachers, teacher);

                if (optionalLinkedTeacher.isPresent() &&
                        !isPairAlreadyAdded(linkedTeachers, teacher, optionalLinkedTeacher.get())) {
                    linkedTeachers.add(Arrays.asList(mapToTeacherResponse(teacher),
                            mapToTeacherResponse(optionalLinkedTeacher.get())));
                }
            }
        }

        return linkedTeachers;
    }

    protected Optional<Teacher> findLinkedTeacher(List<Teacher> teachers, Teacher teacher) {
        return teachers.stream()
                .filter(t -> t.getLinkTeacher() != null && t.getLinkTeacher().equals(teacher))
                .findFirst();
    }

    protected boolean isPairAlreadyAdded(List<List<TeacherResponse>> linkedTeachers,
            Teacher teacher1, Teacher teacher2) {
        return linkedTeachers.stream().anyMatch(pair -> {
            TeacherResponse teacherResponse1 = pair.get(0);
            TeacherResponse teacherResponse2 = pair.get(1);
            return (teacherResponse1.getId() == teacher1.getIdUser() &&
                    teacherResponse2.getId() == teacher2.getIdUser()) ||
                    (teacherResponse1.getId() == teacher2.getIdUser() &&
                            teacherResponse2.getId() == teacher1.getIdUser());
        });
    }

    protected TeacherResponse mapToTeacherResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getIdUser())
                .name(teacher.getName())
                .surname(teacher.getSurname())
                .mailAddress(teacher.getMailAddress())
                .role(teacher.getRole().toString())
                .teacherLinkUserId(teacher.getLinkTeacher() != null ? teacher.getLinkTeacher().getIdUser() : 0)
                .specialities(teacher.getSpecialities())
                .specialitiesStatus(teacher.getSpecilitiesStatus())
                .build();
    }

    public void updateReview(ReviewRequest reviewRequest) {
        Consulting consulting = consultingRepository.getReferenceById(reviewRequest.getIdConsulting());
        consulting.setReview(reviewRequest.getReview());
        consultingRepository.save(consulting);
        log.info("Consulting {} review updated", consulting.getIdConsulting());
    }

    public ConsultingResponse mapToConsultingResponse(Consulting consulting) {
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
        if (consulting.getRoom() != null) {
            respBuilder.setIdRoom(consulting.getRoom().getNumberRoom());
        }
        return respBuilder;

    }

    public List<ConsultingResponse> getConsultings() {
        List<Consulting> projects = consultingRepository.findAll();
        return projects.stream().map(this::mapToConsultingResponse).collect(Collectors.toList());
    }

    public ConsultingResponse getConsulting(int idConsulting) {
        Consulting consulting = consultingRepository.getReferenceById(idConsulting);
        return mapToConsultingResponse(consulting);
    }

    public void addDisponibility(PlanningRequest planningRequest, int idTeacher) {
        Teacher teacher = teacherRepository.getReferenceById(idTeacher);

        Set<Planning> plannings = teacher.getPlannings();
        plannings.add(planningRepository.getReferenceById(planningRequest.getIdPlanning()));
        teacher.setPlannings(plannings);
        teacherRepository.save(teacher);
    }

    public void removeDisponibility(PlanningRequest planningRequest, int idTeacher) {
        Teacher teacher = teacherRepository.getReferenceById(idTeacher);
        Set<Planning> plannings = teacher.getPlannings();
        plannings.remove(planningRepository.getReferenceById(planningRequest.getIdPlanning()));
        teacher.setPlannings(plannings);
        teacherRepository.save(teacher);
    }

    public void setPresentationCommentTeacher(PresentationRequest presRequ) {
        Presentation presentation = presentationRepository.getReferenceById(presRequ.getIdPresentation());
        if (presRequ.getCommentTeacher1() != null)
            presentation.setCommentTeacher1(presRequ.getCommentTeacher1());
        if (presRequ.getCommentTeacher2() != null)
            presentation.setCommentTeacher2(presRequ.getCommentTeacher2());

        presentationRepository.save(presentation);
    }

    public HttpStatus validateSpecialities(StatusRequest validateRequest) {
        Teacher teacher = teacherRepository.getReferenceById(validateRequest.getId());
        if (validateRequest.isValidate()) {
            teacher.setSpecilitiesStatus(SpecialitiesStatus.VALIDATED);
        } else {
            teacher.setSpecilitiesStatus(SpecialitiesStatus.REFUSED);
        }
        teacherRepository.save(teacher);
        return HttpStatus.OK;
    }

}
