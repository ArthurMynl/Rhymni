package com.projet_gl.rhymni.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.dto.LinkedTeamResponse;
import com.projet_gl.rhymni.dto.PairTeamRequest;
import com.projet_gl.rhymni.dto.PresentationRequest;
import com.projet_gl.rhymni.dto.TeamRequest;
import com.projet_gl.rhymni.dto.TeamResponse;
import com.projet_gl.rhymni.dto.TeamStudentRequest;
import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Project;
import com.projet_gl.rhymni.entity.Role;
import com.projet_gl.rhymni.entity.Student;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.PresentationRepository;
import com.projet_gl.rhymni.repository.ProjectRepository;
import com.projet_gl.rhymni.repository.StudentRepository;
import com.projet_gl.rhymni.repository.TeamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    private final StudentRepository studentRepository;

    private final ProjectRepository projectRepository;

    protected final PresentationRepository presentationRepository;

    public List<TeamResponse> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(this::mapToTeamResponse).collect(Collectors.toList());
    }

    public List<TeamResponse> getUnpairedTeams() {
        List<Team> teams = teamRepository.findAll();
        List<Team> unpairedTeams = new ArrayList<>();
        for (Team team : teams)
            if (team.getLinkTeam() == null)
                unpairedTeams.add(team);
        return unpairedTeams.stream().map(this::mapToTeamResponse).collect(Collectors.toList());
    }

    private TeamResponse mapToTeamResponse(Team team) {
        Integer linkedTeamNumber = null;
        Integer projectId = null;
        if (team.getLinkTeam() != null) {
            linkedTeamNumber = team.getLinkTeam().getNumber();
        }
        if (team.getProject() != null) {
            projectId = team.getProject().getIdProject();
        }

        return TeamResponse.builder()
                .number(team.getNumber())
                .idProject(projectId)
                .linkedTeamNumber(linkedTeamNumber)
                .markPresentation(team.getMarkPresentation())
                .markValidation(team.getMarkValidation())
                .linkTestBook(team.getLinkToTestBook())
                .dateModifTest(team.getDateModifTestBook())
                .build();
    }

    public void createTeamPair(PairTeamRequest pairTeamRequest) {
        Team team1 = teamRepository.getReferenceById(pairTeamRequest.getIdLinkedTeam1());
        Team team2 = teamRepository.getReferenceById(pairTeamRequest.getIdLinkedTeam2());
        Team linkedTeam;

        // If the team 1 or 2 is already linked to another team
        if ((team1.getLinkTeam() != null) && (!team1.getLinkTeam().getNumber().equals(team2.getNumber()))) {
            linkedTeam = team1.getLinkTeam();
            linkedTeam.setLinkTeam(null);
            teamRepository.save(linkedTeam);
        }
        if ((team2.getLinkTeam() != null) && (!team2.getLinkTeam().getNumber().equals(team1.getNumber()))) {
            linkedTeam = team2.getLinkTeam();
            linkedTeam.setLinkTeam(null);
            teamRepository.save(linkedTeam);
        }

        // Set the link between the team
        if (!team1.getNumber().equals(team2.getNumber())) {
            team1.setLinkTeam(team2);
            team2.setLinkTeam(team1);
            teamRepository.save(team1);
            teamRepository.save(team2);
            log.info("Team {} / Team {} link established", team1.getNumber(), team2.getNumber());
        }
    }

    public void deleteTeamPair(PairTeamRequest pairTeamRequest) {
        Team team1 = teamRepository.getReferenceById(pairTeamRequest.getIdLinkedTeam1());
        Team team2 = teamRepository.getReferenceById(pairTeamRequest.getIdLinkedTeam2());

        // Set the link between the team
        if (!team1.getNumber().equals(team2.getNumber())) {
            team1.setLinkTeam(null);
            team2.setLinkTeam(null);
            teamRepository.save(team1);
            teamRepository.save(team2);
            log.info("Team {} / Team {} link deleted", team1.getNumber(), team2.getNumber());
        }
    }

    public List<List<TeamResponse>> getTeamPair() {
        List<List<TeamResponse>> pairs = new ArrayList<>();
        List<Team> fullTeams = teamRepository.findAll();
        List<Team> teams = new ArrayList<>();

        // Delete all unpaired teams
        for (Team team : fullTeams)
            if (team.getLinkTeam() != null)
                teams.add(team);

        List<Team> alreadyIn = new ArrayList<>();
        for (Team team : teams) {
            if (!alreadyIn.contains(team)) {
                List<TeamResponse> pair = new ArrayList<>();
                pair.add(mapToTeamResponse(team));
                alreadyIn.add(team);
                pair.add(mapToTeamResponse(team.getLinkTeam()));
                pairs.add(pair);
                alreadyIn.add(team.getLinkTeam());
            }
        }
        return pairs;
    }

    public void registerStudent(TeamStudentRequest teamStudentRequest, int idStudent) {
        Team team = teamRepository.getReferenceById(teamStudentRequest.getIdTeam());

        Student student = studentRepository.getReferenceById(idStudent);

        if (student.getTeam() == null) {
            team.getStudents().add(student);
            student.setTeam(team);
            student.setRole(Role.ROLE_TEAM_MEMBER);
            teamRepository.save(team);
        }
    }

    public LinkedTeamResponse getLinkedTeam(int id) throws EntityNotFoundException {
        Team team = teamRepository.getReferenceById(id);
        if (team.getLinkTeam() == null) {
            return LinkedTeamResponse.builder()
                    .idLinkedTeam(null)
                    .idTeam(team.getNumber())
                    .linkTestBook(team.getLinkToTestBook())
                    .build();
        }
        return LinkedTeamResponse.builder()
                .idLinkedTeam(team.getLinkTeam().getNumber())
                .idTeam(team.getNumber())
                .linkTestBook(team.getLinkTeam().getLinkToTestBook())
                .build();
    }

    public TeamResponse getTeamByProject(int id) throws EntityNotFoundException {
        Project project = projectRepository.getReferenceById(id);
        Team team = teamRepository.findByProject(project);
        if (team == null) {
            throw new EntityNotFoundException("Team not found for this project ID: " + id);
        }
        return mapToTeamResponse(team);
    }

    public void modifyTestBook(TeamRequest teamRequest) {
        Team team = teamRepository.getReferenceById(teamRequest.getTeamNumber());
        team.setLinkToTestBook(teamRequest.getLinkTestBook());
        team.setDateModifTestBook(LocalDateTime.now());
        teamRepository.save(team);
    }

    public void removeStudent(TeamStudentRequest teamStudentRequest, int idStudent) {
        Team team = teamRepository.getReferenceById(teamStudentRequest.getIdTeam());

        Student student = studentRepository.getReferenceById(idStudent);

        if (student.getTeam() != null) {
            team.getStudents().remove(student);
            student.setTeam(null);
            student.setRole(Role.ROLE_STUDENT);
            teamRepository.save(team);
        }
    }

    public void setcommentTeam(PresentationRequest presRequ) {
        Presentation presentation = presentationRepository.getReferenceById(presRequ.getIdPresentation());
        if (presRequ.getCommentTeam1() != null)
            presentation.setCommentTeam1(presRequ.getCommentTeam1());
        if (presRequ.getCommentTeam2() != null)
            presentation.setCommentTeam2(presRequ.getCommentTeam2());

        presentationRepository.save(presentation);
    }

    public void setMarkTeam(TeamRequest teamRequest) {
        Team team = teamRepository.getReferenceById(teamRequest.getTeamNumber());
        if (teamRequest.getMarkPresentation() != null) {
            team.setMarkPresentation(teamRequest.getMarkPresentation());
        }
        if (teamRequest.getMarkValidation() != null) {
            team.setMarkValidation(teamRequest.getMarkValidation());
        }
        teamRepository.save(team);
    }

    public TeamResponse getTeamByNumber(int number) {
        Team team = teamRepository.getReferenceById(number);
        return mapToTeamResponse(team);
    }
}
