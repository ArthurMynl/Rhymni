package com.projet_gl.rhymni.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    
}
