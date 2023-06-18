package com.projet_gl.rhymni.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

class FileTest {

    @Test
    void testGettersAndSetters() {
        String code = "FILE123";
        String name = "example.txt";
        File file = new File();
        file.setCode(code);
        file.setName(name);

        assertEquals(code, file.getCode());
        assertEquals(name, file.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        File file1 = File.builder()
                            .code("FILE123")
                            .name("example.txt")
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();
        File file2 = File.builder()
                            .code("FILE123")
                            .name("example.txt")
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();
        File file3 = File.builder()
                            .code("FILE456")
                            .name("example2.txt")
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();
        File file4 = File.builder()
                            .code("FILE123")
                            .name(null)
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();
        File file5 = File.builder()
                            .code(null)
                            .name("example.txt")
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();
        File file6 = File.builder()
                            .code(null)
                            .name(null)
                            .date(LocalDateTime.of(2023, 05, 12, 12, 30, 10))
                            .build();

        assertEquals(file1, file1);
        assertEquals(file1, file2);
        assertEquals(file1.hashCode(), file2.hashCode());

        assertNotEquals(file1, file3);
        assertNotEquals(file1.hashCode(), file3.hashCode());

        assertNotEquals(file1, file4);
        assertNotEquals(file1, file5);
        assertNotEquals(file1, file6);
        assertNotEquals(file1, new Object());
        assertNull(file6.getCode());
        assertNull(file6.getName());
    }

    @Test
    void testToString() {
        String code = "FILE123";
        String name = "example.txt";
        LocalDateTime date = LocalDateTime.of(2023, 05, 18, 11, 33, 32);

        File file = new File(code, name, date);

        String expectedToString = "File(code=FILE123, name=example.txt, date=2023-05-18T11:33:32)";
        assertEquals(expectedToString, file.toString());
    }
}
