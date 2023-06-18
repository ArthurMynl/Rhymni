package com.projet_gl.rhymni.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterInfoRequest {
    private Integer semesterId;
    private LocalDate startDate;
    private LocalDate endDate;
}
