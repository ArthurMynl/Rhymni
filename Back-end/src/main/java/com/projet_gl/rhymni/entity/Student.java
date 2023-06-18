package com.projet_gl.rhymni.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Student extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_number", nullable = true)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "id_option", nullable = true)
    private Options option;

    @Column(name = "student_mark", length = 10, nullable = true)
    private Float studentMark;

    @Column(name = "jury_bonus", length = 10, nullable = true)
    private Float juryBonus;

    @Column(name = "option_leader_bonus", length = 10, nullable = true)
    private Float optionLeaderBonus;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "studentReceivers")
    private Set<Notification> notifications;

    public Student(Team team, Options option, Float studentMark, Float juryBonus, Float optionLeaderBonus,
            Set<Notification> notifications) {
        this.team = team;
        this.option = option;
        this.studentMark = studentMark;
        this.juryBonus = juryBonus;
        this.optionLeaderBonus = optionLeaderBonus;
        this.notifications = notifications;
    }

    @Builder(builderMethodName = "userBuilder")
    public Student(Integer idUser, String name, String surname, String mailAddress, Role role, String password) {
        super(idUser, name, surname, mailAddress, role, password);
    }

}
