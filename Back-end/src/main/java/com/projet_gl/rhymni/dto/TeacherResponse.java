package com.projet_gl.rhymni.dto;

import java.util.Set;

import com.projet_gl.rhymni.entity.SpecialitiesStatus;
import com.projet_gl.rhymni.entity.Speciality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponse {
    private int id;
    private String name;
    private String surname;
    private String mailAddress;
    private String role;
    private int teacherLinkUserId;
    Set<Speciality> specialities;
    private SpecialitiesStatus specialitiesStatus;

}
