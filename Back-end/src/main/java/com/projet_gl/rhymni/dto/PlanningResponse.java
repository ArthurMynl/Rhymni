package com.projet_gl.rhymni.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningResponse {
    private Integer idPlanning;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private List<Integer> teachersID;
}
