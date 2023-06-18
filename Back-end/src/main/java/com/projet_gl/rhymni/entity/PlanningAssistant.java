package com.projet_gl.rhymni.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "planningAssistant")
@Getter
@Setter
@NoArgsConstructor
public class PlanningAssistant extends User {
    
    public PlanningAssistant(Integer idUser, String name, String surname, String mailAddress, String password) {
        super(idUser, name, surname, mailAddress, Role.ROLE_PLANNING_ASSISTANT, password);
    }
}
