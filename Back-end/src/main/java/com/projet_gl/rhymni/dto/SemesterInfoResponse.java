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
public class SemesterInfoResponse {
    private Integer idSemesterInfo;
    private LocalDate startDate;
    private LocalDate endDate;
}
