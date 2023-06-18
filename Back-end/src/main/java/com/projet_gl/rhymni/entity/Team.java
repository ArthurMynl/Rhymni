package com.projet_gl.rhymni.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@AllArgsConstructor
@Builder
public class Team implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "number", length = 11, nullable = false)
    private Integer number;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "team_link_number", nullable = true)
    @JsonIgnore
    private Team linkTeam;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    @Column(name = "mark_presentation", length = 10, nullable = true)
    private Float markPresentation;

    @Column(name = "mark_validation", length = 10, nullable = true)
    private Float markValidation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    @JsonIgnore
    private Set<Student> students;

    @Column(name = "date_modif_test", length = 11, nullable = true)
    @JsonIgnore
    private LocalDateTime dateModifTestBook;

    @Column(name = "link_to_test", length = 200, nullable = true)
    @JsonIgnore
    private String linkToTestBook;
}
