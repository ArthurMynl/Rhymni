package com.projet_gl.rhymni.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findByTeamNotNull();

    List<Student> findByTeamNumber(Integer teamId);
    Optional<Student> findByMailAddress(String mailAddress);
}
