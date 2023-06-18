package com.projet_gl.rhymni.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresentationRequest {
    private int idPresentation;
    private String date;
    private String hours;
    private int idRoom;
    private int idTeacher;
    private int idTeam;
    private String commentTeam1;
    private String commentTeacher1;
    private String commentTeam2;
    private String commentTeacher2;
}
