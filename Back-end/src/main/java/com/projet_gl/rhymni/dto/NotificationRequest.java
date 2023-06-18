package com.projet_gl.rhymni.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Integer idNotification;
    private Integer personneSend;
    private String speciality;
    private String message;
    private Integer idPlanning;
}