package com.projet_gl.rhymni.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "presentation")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Presentation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "id_presentation", nullable = false)
    private Integer idPresentation;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "date_hours", length = 50, nullable = false)
    private LocalDateTime dateHours;

    @Column(name = "comment_team1", length = 100, nullable = true)
    private String commentTeam1;

    @Column(name = "comment_teacher1", length = 100, nullable = true)
    private String commentTeacher1;

    @Column(name = "comment_team2", length = 100, nullable = true)
    private String commentTeam2;

    @Column(name = "comment_teacher2", length = 100, nullable = true)
    private String commentTeacher2;

    @ManyToMany()
    @JsonIgnore
    @JoinTable(name = "teacher_presentation", joinColumns = @JoinColumn(name = "presentation_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Teacher> teacher;

    @ManyToMany()
    @JsonIgnore
    @JoinTable(name = "team_presentation", joinColumns = @JoinColumn(name = "presentation_id"), inverseJoinColumns = @JoinColumn(name = "team_id"))
    private Set<Team> teams;

    @ManyToOne()
    @JsonIgnore
    @JoinTable(name = "room_presentation", joinColumns = @JoinColumn(name = "presentation_id"), inverseJoinColumns = @JoinColumn(name = "room_number"))
    private Room room;

}
