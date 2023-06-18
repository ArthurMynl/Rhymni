package com.projet_gl.rhymni.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consulting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "id_consulting", nullable = false)
    private Integer idConsulting;

    @Column(name = "team")
    private Integer idTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_planning", referencedColumnName = "id_planning")
    private Planning planning;

    @Column(name = "review", length = 250, nullable = true)
    private String review;

    @Column(name = "specility_consulting", length = 250, nullable = true)
    private String specialityConsulting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "id_room", nullable = true)
    private Room room;
}
