package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

}
