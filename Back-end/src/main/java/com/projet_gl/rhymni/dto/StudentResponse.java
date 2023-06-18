package com.projet_gl.rhymni.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Integer id;
    private String name;
    private String surname;
    private String mailAddress;
    private Integer idTeam;
    private String option;
    private Float studentMark;
    private Float juryBonus;
    private Float optionLeaderBonus;
}
