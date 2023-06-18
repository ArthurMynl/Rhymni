package com.projet_gl.rhymni.dto;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class LinkedTeamResponse {
    private Integer idTeam;
    @Nullable
    private Integer idLinkedTeam;
    private String linkTestBook;
}
