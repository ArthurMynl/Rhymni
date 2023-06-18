package com.projet_gl.rhymni.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class SemesterInfoTest {

    private SemesterInfo semesterInfo;

    @BeforeEach
    void setUp() {
        semesterInfo = new SemesterInfo();
        semesterInfo.setIdSemesterInfo(1);
        semesterInfo.setStartDate(LocalDate.of(2023, 1, 1));
        semesterInfo.setEndDate(LocalDate.of(2023, 6, 30));
    }

    @Test
    void testSemesterInfoGetters() {
        Assertions.assertEquals(1, semesterInfo.getIdSemesterInfo());
        Assertions.assertEquals(LocalDate.of(2023, 1, 1), semesterInfo.getStartDate());
        Assertions.assertEquals(LocalDate.of(2023, 6, 30), semesterInfo.getEndDate());
    }
}

