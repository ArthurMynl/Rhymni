package com.projet_gl.rhymni.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "planning")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planning implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "id_planning", length = 11, nullable = false)
    private Integer idPlanning;

    @Column(name = "date_start", length = 11, nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "date_end", length = 11, nullable = false)
    private LocalDateTime dateEnd;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "consuting_planning", joinColumns = @JoinColumn(name = "planning_id"), inverseJoinColumns = @JoinColumn(name = "consulting_id"))
    private Set<Consulting> consultings;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "teacher_planning", joinColumns = @JoinColumn(name = "planning_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<Teacher> teachers;

    public Planning(Integer idPlanning, LocalDateTime dateStart, LocalDateTime dateEnd) {
        this.idPlanning = idPlanning;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}
