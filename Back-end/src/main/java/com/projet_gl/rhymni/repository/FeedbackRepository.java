package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {

}
