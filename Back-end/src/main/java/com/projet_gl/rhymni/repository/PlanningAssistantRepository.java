package com.projet_gl.rhymni.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.PlanningAssistant;

public interface PlanningAssistantRepository extends JpaRepository<PlanningAssistant, Integer> {

    Optional<PlanningAssistant> findByMailAddress(String mailAddress);
}