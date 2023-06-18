package com.projet_gl.rhymni.dto;

import com.projet_gl.rhymni.entity.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatusResponse {
    private int idProject;
    private ProjectStatus status;
}