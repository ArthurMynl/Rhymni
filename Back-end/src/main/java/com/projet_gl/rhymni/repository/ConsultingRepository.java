package com.projet_gl.rhymni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Consulting;

public interface ConsultingRepository extends JpaRepository<Consulting, Integer> {
    List<Consulting> findAllByTeacherIdUser(Integer teacherId);
}
