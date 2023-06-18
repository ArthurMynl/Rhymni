package com.projet_gl.rhymni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Planning;

public interface PlanningRepository extends JpaRepository<Planning, Integer> {

    List<Planning> findAllByTeachersIdUser(Integer idTeacher);

}
