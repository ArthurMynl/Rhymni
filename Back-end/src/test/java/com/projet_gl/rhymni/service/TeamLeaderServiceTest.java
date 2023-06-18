package com.projet_gl.rhymni.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projet_gl.rhymni.dto.PairTeamRequest;
import com.projet_gl.rhymni.entity.Team;
import com.projet_gl.rhymni.repository.TeamRepository;

@ExtendWith(MockitoExtension.class)
class TeamLeaderServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamLeaderService;

    private Team team1;
    private Team team2;
    private Team team3;

    @BeforeEach
    public void setUp() {
        team1 = Team.builder()
                .number(1)
                .build();
        team2 = Team.builder()
                .number(2)
                .build();
        team3 = Team.builder()
                .number(3)
                .build();
    }

    @Test
    void testCreateTeamPair() {
        when(teamRepository.getReferenceById(1)).thenReturn(team1);
        when(teamRepository.getReferenceById(2)).thenReturn(team2);

        PairTeamRequest pairTeamRequest = new PairTeamRequest();
        pairTeamRequest.setIdLinkedTeam1(1);
        pairTeamRequest.setIdLinkedTeam2(2);

        teamLeaderService.createTeamPair(pairTeamRequest);

        verify(teamRepository, times(1)).getReferenceById(1);
        verify(teamRepository, times(1)).getReferenceById(2);
        verify(teamRepository, times(1)).save(team1);
        verify(teamRepository, times(1)).save(team2);
    }

    @Test
    void testCreateTeamPairWithExistingLink() {
        team1.setLinkTeam(team3);
        team2.setLinkTeam(team3);

        when(teamRepository.getReferenceById(1)).thenReturn(team1);
        when(teamRepository.getReferenceById(2)).thenReturn(team2);

        PairTeamRequest pairTeamRequest = new PairTeamRequest();
        pairTeamRequest.setIdLinkedTeam1(1);
        pairTeamRequest.setIdLinkedTeam2(2);

        teamLeaderService.createTeamPair(pairTeamRequest);

        verify(teamRepository, times(1)).getReferenceById(1);
        verify(teamRepository, times(1)).getReferenceById(2);
        verify(teamRepository, times(4)).save(any(Team.class));
    }
}
