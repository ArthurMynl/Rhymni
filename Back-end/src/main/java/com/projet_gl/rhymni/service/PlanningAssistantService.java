package com.projet_gl.rhymni.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.projet_gl.rhymni.dto.PlanningResponse;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.PresentationResponse;
import com.projet_gl.rhymni.dto.RoomResponse;
import com.projet_gl.rhymni.dto.SemesterInfoRequest;
import com.projet_gl.rhymni.dto.SemesterInfoResponse;
import com.projet_gl.rhymni.entity.Planning;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Room;
import com.projet_gl.rhymni.entity.SemesterInfo;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.PlanningRepository;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.RoomRepository;
import com.projet_gl.rhymni.repository.SemesterInfoRepository;
import com.projet_gl.rhymni.repository.TeacherRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanningAssistantService {

        private final TeamRepository teamRepository;

        private final PresentationRepository presentationRepository;

        private final RoomRepository roomRepository;

        private final TeacherRepository teacherRepository;

        private final PlanningRepository planningRepository;

        private final SemesterInfoRepository semesterRepository;

        private static final String PRESENTATION_INTER = "Soutenance intermédiaire";
        private static final String PRESENTATION_FINALE = "Soutenance finale";
        private static final String PROF_PRESENTATION = "Le professeur a déjà une soutenance pour cet horaire.";
        private static final String TEAM_HAS = "L'équipe à déjà une ";

        protected ResponseEntity<String> handleError(IllegalArgumentException e) {
                String errorMessage = e.getMessage();

                if (errorMessage.contains("Le professeur a déjà une soutenance")) {
                        return ResponseEntity.badRequest().body(errorMessage);
                } else if (errorMessage.contains(TEAM_HAS + PRESENTATION_INTER) ||
                                errorMessage.contains(TEAM_HAS + PRESENTATION_FINALE)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                } else if (errorMessage.contains("L'équipe n'à pas de soutenance intermédiaire")) {
                        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorMessage);
                } else if (errorMessage
                                .contains("Une soutenance finale doit être après la soutenance intermédiaire") ||
                                errorMessage.contains(
                                                "Une soutenance intermédiaire doit être avant la soutenance finale")) {
                        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(errorMessage);
                } else if (errorMessage.contains(
                                "L'équipe à une soutenance finale, supprimez là avant de supprimer la soutenance intermédiaire")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
                } else {
                        return ResponseEntity.badRequest().body("Erreur inconnue");
                }
        }

        public List<PresentationResponse> getAllPresentations() {
                List<Presentation> presentation = presentationRepository.findAll();

                return presentation.stream().map(this::mapToPresentationResponse).collect(Collectors.toList());
        }

        protected PresentationResponse mapToPresentationResponse(Presentation presentation) {

                return PresentationResponse.builder()
                                .idPresentation(presentation.getIdPresentation())
                                .type(presentation.getType())
                                .dateHours(presentation.getDateHours())
                                .commentTeam1(presentation.getCommentTeam1())
                                .commentTeacher1(presentation.getCommentTeacher1())
                                .commentTeam2(presentation.getCommentTeam2())
                                .commentTeacher2(presentation.getCommentTeacher2())
                                .teacher(presentation.getTeacher())
                                .teams(presentation.getTeams())
                                .room(presentation.getRoom())
                                .build();
        }

        public List<RoomResponse> getAllRooms() {
                List<Room> room = roomRepository.findAll();

                return room.stream().map(this::mapToRoomResponse).collect(Collectors.toList());
        }

        protected RoomResponse mapToRoomResponse(Room room) {

                return RoomResponse.builder()
                                .numberRoom(room.getNumberRoom())
                                .name(room.getName())
                                .build();
        }

        protected boolean isTeamAvailable(String type, Integer number) {

                Team team = teamRepository.getReferenceById(number);

                Presentation existingPresentation = presentationRepository.findByTypeAndTeams(type, team);

                return existingPresentation == null;
        }

        protected boolean isTeacherAvailable(String date, String hours, int idTeacher) {

                Teacher teacher = teacherRepository.getReferenceById(idTeacher);

                Presentation existingPresentation = presentationRepository.findByDateHoursAndTeacher(
                                LocalDateTime.parse(date + "T" + hours), teacher);

                Presentation existingLinkedTeacherPresentation = presentationRepository.findByDateHoursAndTeacher(
                                LocalDateTime.parse(date + "T" + hours), teacher.getLinkTeacher());

                return existingPresentation == null && existingLinkedTeacherPresentation == null;
        }

        public HttpStatus importConsultingTimeSlot(MultipartFile csvFile)
                        throws CsvValidationException, NumberFormatException {
                String[] line;
                try {
                        // parsing a CSV file into BufferedReader class constructor
                        CSVReader reader = new CSVReaderBuilder(new InputStreamReader(csvFile.getInputStream()))
                                        .build();
                        line = reader.readNext();
                        for (String token : line) {
                                String[] header = token.split(";"); // semi-column as separator
                                if (!"DateDebut".equals(header[0]) || !"DateFin".equals(header[1])) {
                                        reader.close();
                                        return HttpStatus.BAD_REQUEST;
                                }
                        }

                        while ((line = reader.readNext()) != null) {
                                for (String token : line) {
                                        String[] listConsultingTimeSlot = token.split(";");
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                        LocalDateTime startDate = LocalDateTime.parse(listConsultingTimeSlot[0],
                                                        formatter);
                                        LocalDateTime endDate = LocalDateTime.parse(listConsultingTimeSlot[1],
                                                        formatter);

                                        Planning planning = Planning.builder()
                                                        .dateStart(startDate)
                                                        .dateEnd(endDate)
                                                        .build();
                                        planningRepository.save(planning);
                                }
                        }
                        reader.close();
                        return HttpStatus.CREATED;
                } catch (IOException e) {
                        return HttpStatus.INTERNAL_SERVER_ERROR;
                }
        }

        public ResponseEntity<String> createIntermediairePresentation(PresentationRequest presentationRequest) {
                try {
                        checkAvailability(presentationRequest, PRESENTATION_INTER);
                        Presentation presentation = createPresentation(presentationRequest,
                                        PRESENTATION_INTER);
                        presentationRepository.save(presentation);
                        return ResponseEntity.created(URI.create("/planning/createIntermediatePresentation/"))
                                        .body("Soutenance intermédiaire créée avec succès.");
                } catch (IllegalArgumentException e) {
                        return handleError(e);
                }
        }

        protected void checkAvailability(PresentationRequest presentationRequest, String type) {
                Teacher teacher = teacherRepository.getReferenceById(presentationRequest.getIdTeacher());
                Team team = teamRepository.getReferenceById(presentationRequest.getIdTeam());

                if (!isTeacherAvailable(presentationRequest.getDate(), presentationRequest.getHours(),
                                teacher.getIdUser())) {
                        throw new IllegalArgumentException(PROF_PRESENTATION);
                } else if (!isTeamAvailable(type, team.getNumber())) {
                        throw new IllegalArgumentException(TEAM_HAS + type);
                }
        }

        protected Presentation createPresentation(PresentationRequest presentationRequest, String type) {
                Teacher teacher = teacherRepository.getReferenceById(presentationRequest.getIdTeacher());
                Team team = teamRepository.getReferenceById(presentationRequest.getIdTeam());
                Room room = roomRepository.getReferenceById(presentationRequest.getIdRoom());

                Set<Teacher> teacherSet = createTeacherSet(teacher); // Utilisez la méthode createTeacherSet ici

                Set<Team> teamSet = new HashSet<>();
                teamSet.add(team);
                teamSet.add(team.getLinkTeam());

                return Presentation.builder()
                                .type(type)
                                .dateHours(LocalDateTime.parse(
                                                presentationRequest.getDate() + "T" + presentationRequest.getHours()))
                                .teacher(teacherSet)
                                .teams(teamSet)
                                .room(room)
                                .build();
        }

        public ResponseEntity<String> createFinalPresentation(PresentationRequest presentationRequest) {
                try {
                        checkFinalAvailability(presentationRequest);
                        Presentation presentation = createPresentation(presentationRequest, PRESENTATION_FINALE);
                        presentationRepository.save(presentation);
                        return ResponseEntity.ok().body("Soutenance finale créée avec succès.");
                } catch (IllegalArgumentException e) {
                        return handleError(e);
                }
        }

        protected void checkFinalAvailability(PresentationRequest presentationRequest) {
                checkAvailability(presentationRequest, PRESENTATION_FINALE);
                Team team = teamRepository.getReferenceById(presentationRequest.getIdTeam());

                if (isTeamAvailable(PRESENTATION_INTER, team.getNumber())) {
                        throw new IllegalArgumentException("L'équipe n'à pas de soutenance intermédiaire.");
                }

                Presentation intermediatePresentation = presentationRepository
                                .findByTypeAndTeams(PRESENTATION_INTER, team);

                LocalDateTime dateHours = LocalDateTime
                                .parse(presentationRequest.getDate() + "T" + presentationRequest.getHours());

                if (dateHours.compareTo(intermediatePresentation.getDateHours()) <= 0) {
                        throw new IllegalArgumentException(
                                        "Une soutenance finale doit être après la soutenance intermédiaire");
                }
        }

        public ResponseEntity<String> deletePresentation(PresentationRequest presentationRequest) {
                try {
                        Presentation presentation = presentationRepository
                                        .getReferenceById(presentationRequest.getIdPresentation());
                        Team team = teamRepository.getReferenceById(presentationRequest.getIdTeam());

                        if (PRESENTATION_INTER.equals(presentation.getType())
                                        && !isTeamAvailable(PRESENTATION_FINALE, team.getNumber())) {
                                throw new IllegalArgumentException(
                                                "L'équipe à une soutenance finale, supprimez là avant de supprimer la soutenance intermédiaire.");
                        }

                        deletePresentationAndAssociations(presentation);
                        return ResponseEntity.ok().body(presentation.getType() + "  supprimée avec succès.");
                } catch (IllegalArgumentException e) {
                        return handleError(e);
                }
        }

        protected void deletePresentationAndAssociations(Presentation presentation) {
                presentation.setTeams(null);
                presentation.setTeacher(null);
                presentation.setRoom(null);
                presentationRepository.delete(presentation);
        }

        public List<PlanningResponse> getAllPlanning() {
                List<PlanningResponse> listResponse = new ArrayList<>();
                for (Planning planning : planningRepository.findAll())
                        listResponse.add(planningToPlanningResponse(planning));
                return listResponse;
        }

        protected PlanningResponse planningToPlanningResponse(Planning planning) {
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

        public List<PlanningResponse> getAllPlanningForTeacher(Integer idTeacher) {
                List<Planning> listPlanning = planningRepository.findAllByTeachersIdUser(idTeacher);
                List<PlanningResponse> listResponse = new ArrayList<>();
                for (Planning planning : listPlanning)
                        listResponse.add(planningToPlanningResponse(planning));
                return listResponse;
        }

        public ResponseEntity<String> modifyIntermediairePresentation(PresentationRequest presentationRequest) {

                try {
                        Teacher teacher = teacherRepository.getReferenceById(presentationRequest.getIdTeacher());

                        if (!isTeacherAvailable(presentationRequest.getDate(), presentationRequest.getHours(),
                                        teacher.getIdUser())) {
                                throw new IllegalArgumentException(
                                                PROF_PRESENTATION);
                        }

                        Team team = teamRepository.getReferenceById(presentationRequest.getIdTeam());

                        Presentation presentation = presentationRepository
                                        .getReferenceById(presentationRequest.getIdPresentation());

                        Presentation finalPresentation = presentationRepository
                                        .findByTypeAndTeams(PRESENTATION_FINALE, team);

                        LocalDateTime dateHours = parseDateHours(presentationRequest.getDate(),
                                        presentationRequest.getHours());

                        // Check if a final presentation exists and if the date is before the final
                        if (finalPresentation != null && (dateHours.compareTo(finalPresentation.getDateHours()) > 0 ||
                                        dateHours.compareTo(finalPresentation.getDateHours()) == 0)) {
                                throw new IllegalArgumentException(
                                                "Une soutenance intermédiaire doit être avant la soutenance finale.");
                        }

                        Room room = roomRepository.getReferenceById(presentationRequest.getIdRoom());

                        Set<Teacher> teacherSet = createTeacherSet(teacher);

                        presentation.setDateHours(dateHours);
                        presentation.setRoom(room);
                        presentation.setTeacher(teacherSet);

                        presentationRepository.save(presentation);
                        return ResponseEntity.ok().body("Soutenance intermédiaire modifiée avec succès.");
                } catch (IllegalArgumentException e) {
                        return handleError(e);
                }
        }

        protected LocalDateTime parseDateHours(String date, String hours) {
                return LocalDateTime.parse(date + "T" + hours);
        }

        protected Set<Teacher> createTeacherSet(Teacher teacher) {
                Set<Teacher> teacherSet = new HashSet<>();
                teacherSet.add(teacher);
                teacherSet.add(teacher.getLinkTeacher());
                return teacherSet;
        }

        public ResponseEntity<String> modifyFinalPresentation(PresentationRequest presentationRequest) {
                try {
                        Presentation presentation = getAndValidatePresentation(presentationRequest);
                        LocalDateTime dateHours = getAndValidateDateHours(presentationRequest);

                        Presentation intermediatePresentation = presentationRepository.findByTypeAndTeams(
                                        PRESENTATION_INTER, presentation.getTeams().iterator().next());
                        if (dateHours.compareTo(intermediatePresentation.getDateHours()) <= 0) {
                                throw new IllegalArgumentException(
                                                "Une soutenance finale doit être après la soutenance intermédiaire.");
                        }

                        presentation.setDateHours(dateHours);
                        presentationRepository.save(presentation);
                        return ResponseEntity.ok().body("Soutenance finale modifiée avec succès.");
                } catch (IllegalArgumentException e) {
                        return handleError(e);
                }
        }

        protected Presentation getAndValidatePresentation(PresentationRequest presentationRequest) {
                Teacher teacher = teacherRepository.getReferenceById(presentationRequest.getIdTeacher());
                if (!isTeacherAvailable(presentationRequest.getDate(), presentationRequest.getHours(),
                                teacher.getIdUser())) {
                        throw new IllegalArgumentException(PROF_PRESENTATION);
                }

                Presentation presentation = presentationRepository
                                .getReferenceById(presentationRequest.getIdPresentation());
                Room room = roomRepository.getReferenceById(presentationRequest.getIdRoom());

                Set<Teacher> teacherSet = new HashSet<>();
                teacherSet.add(teacher);
                teacherSet.add(teacher.getLinkTeacher());
                presentation.setTeacher(teacherSet);
                presentation.setRoom(room);

                return presentation;
        }

        protected LocalDateTime getAndValidateDateHours(PresentationRequest presentationRequest) {
                return LocalDateTime.parse(presentationRequest.getDate() + "T" + presentationRequest.getHours());
        }

        public void updateStartDateProject(SemesterInfoRequest semesterRequest) {
                SemesterInfo semester = semesterRepository.getReferenceById(semesterRequest.getSemesterId());
                semester.setStartDate(semesterRequest.getStartDate());
                semesterRepository.save(semester);
        }

        public void updateEndDateProject(SemesterInfoRequest semesterRequest) {
                SemesterInfo semester = semesterRepository.getReferenceById(semesterRequest.getSemesterId());
                semester.setEndDate(semesterRequest.getEndDate());
                semesterRepository.save(semester);
        }

        public SemesterInfoResponse getSemesterActual() {
                SemesterInfo semester = semesterRepository.getReferenceById(LocalDate.now().getYear());
                return mapToSemesterResponse(semester);        
        }

        private SemesterInfoResponse mapToSemesterResponse(SemesterInfo semester) {
                return SemesterInfoResponse.builder()
                        .idSemesterInfo(semester.getIdSemesterInfo())
                        .startDate(semester.getStartDate())
                        .endDate(semester.getEndDate())
                        .build();
        }

}
