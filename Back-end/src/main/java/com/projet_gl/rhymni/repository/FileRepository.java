package com.projet_gl.rhymni.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projet_gl.rhymni.entity.File;

public interface FileRepository extends JpaRepository<File, String>{
    
    public File findByName(String name);

    public List<File> findAllByNameOrderByDateDesc(String fileName);
}
