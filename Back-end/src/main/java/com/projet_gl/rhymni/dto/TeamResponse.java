package com.projet_gl.rhymni.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {
    private Integer number;
    private Integer idProject;
    private Integer linkedTeamNumber;
    private String linkTestBook;
    private LocalDateTime dateModifTest;
    private Float markPresentation;
    private Float markValidation;
}
