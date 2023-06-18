package com.projet_gl.rhymni.dto;

import java.time.LocalDate;

import com.projet_gl.rhymni.entity.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private int id;
    private String name;
    private String description;
    private ProjectStatus status; 
    private String rejectComment;
    private LocalDate startDate;
    private Integer idSemesterInfo;
}
