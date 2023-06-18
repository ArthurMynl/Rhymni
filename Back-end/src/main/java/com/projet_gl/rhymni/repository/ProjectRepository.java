package com.projet_gl.rhymni.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.projet_gl.rhymni.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT t.id FROM Project p JOIN p.teachers t WHERE p.idProject = :projectId")
    Set<Integer> findTeacherIdsByProjectId(@Param("projectId") Integer projectId);
}
