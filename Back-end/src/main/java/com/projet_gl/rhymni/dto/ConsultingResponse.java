package com.projet_gl.rhymni.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultingResponse {
    private int idConsulting;
    private int idTeam;
    private int idTeacher;
    private int idPlanning;
    private String speciality;
    private String review;
    private int idRoom;
}
