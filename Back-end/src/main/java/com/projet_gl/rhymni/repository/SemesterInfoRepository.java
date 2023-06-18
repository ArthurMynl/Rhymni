package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.SemesterInfo;

public interface SemesterInfoRepository extends JpaRepository<SemesterInfo, Integer> {
    
}
