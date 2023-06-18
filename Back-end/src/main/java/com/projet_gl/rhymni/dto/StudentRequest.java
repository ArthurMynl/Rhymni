package com.projet_gl.rhymni.dto;

import com.projet_gl.rhymni.entity.Options;
import com.projet_gl.rhymni.entity.Team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequest {
    private Integer id;
    private String name;
    private String surname;
    private String mailAddress;
    private Team team;
    private Options option;
    private Float studentMark;
    private Float juryBonus;
    private Float optionLeaderBonus;
}
