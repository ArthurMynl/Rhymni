package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Teacher;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByMailAddress(String mailAddress);
}
