package com.projet_gl.rhymni.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.projet_gl.rhymni.entity.Room;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresentationResponse {
    private Integer idPresentation;
    private String type;
    private LocalDateTime dateHours;
    private String commentTeam1;
    private String commentTeacher1;
    private String commentTeam2;
    private String commentTeacher2;
    private Set<Teacher> teacher;
    private Set<Team> teams;
    private Room room;
}
