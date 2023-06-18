package com.projet_gl.rhymni.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserInfo {
    private Integer userId;
    private String mailAddress;
    private String role;
    private Integer idTeam;
}  
