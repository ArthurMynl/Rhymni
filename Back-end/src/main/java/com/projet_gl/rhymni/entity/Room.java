package com.projet_gl.rhymni.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // auto-increment
    @Column(name = "id_room", nullable = false)
    private Integer numberRoom;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @JsonIgnore
    private Set<Presentation> presentations;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "room")
    @JsonIgnore
    private Set<Consulting> consultings;
}
