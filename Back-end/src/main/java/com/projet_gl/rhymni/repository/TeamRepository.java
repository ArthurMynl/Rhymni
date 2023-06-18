package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Project;
import com.projet_gl.rhymni.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByProject(Project project);
}
