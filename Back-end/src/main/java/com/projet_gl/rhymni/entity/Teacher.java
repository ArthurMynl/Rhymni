package com.projet_gl.rhymni.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Teacher extends User {
    @Column(name = "specialties", length = 20, nullable = false)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "teacher_speciality", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Speciality> specialities;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JsonIgnore
    @JoinColumn(name = "teacher_link_user_id", nullable = true)
    private Teacher linkTeacher;

    @Builder
    public Teacher(Integer idUser, String name, String surname, String mailAddress, String password,
            Set<Speciality> specialities) {
        super(idUser, name, surname, mailAddress, Role.ROLE_TEACHER, password);
        this.specialities = specialities;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SpecialitiesStatus specilitiesStatus = SpecialitiesStatus.PENDING;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "teacher_project", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    @JsonIgnore
    private Set<Presentation> presentations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teacher")
    @JsonIgnore
    private Set<Consulting> consultings;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "teacherReceivers")
    @JsonIgnore
    private Set<Notification> notifications;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "teacher_planning", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "planning_id"))
    private Set<Planning> plannings;

}
