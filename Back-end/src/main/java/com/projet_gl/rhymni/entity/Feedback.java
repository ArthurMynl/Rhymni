package com.projet_gl.rhymni.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feedback implements Serializable {
    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fileId", nullable = false)
    private String fileId;

    @Column(name = "comment", length = 255, nullable = false)
    private String comment;

}
