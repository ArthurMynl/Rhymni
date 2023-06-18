package com.projet_gl.rhymni.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "semester_info")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterInfo implements Serializable  {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "id_semester_info", nullable = false)
    private Integer idSemesterInfo;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


}
