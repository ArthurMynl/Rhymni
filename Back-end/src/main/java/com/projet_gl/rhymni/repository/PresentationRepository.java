package com.projet_gl.rhymni.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Presentation;
import com.projet_gl.rhymni.entity.Teacher;
import com.projet_gl.rhymni.entity.Team;

public interface PresentationRepository extends JpaRepository<Presentation, Integer> {
    Presentation findByDateHoursAndTeacher(LocalDateTime dateHours, Teacher teacher);
    Presentation findByTypeAndTeams(String type, Team team);
}
