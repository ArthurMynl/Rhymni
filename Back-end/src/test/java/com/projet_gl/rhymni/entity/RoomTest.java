package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RoomTest {

    @Test
    void testCreateRoom() {
        // Create a Room object

        Presentation presentation = Presentation.builder()
                .idPresentation(1)
                .build();

        Consulting consulting = Consulting.builder()
                .idConsulting(1)
                .build();

        Set<Presentation> listPresentation = new HashSet<>();
        listPresentation.add(presentation);
        Set<Consulting> listConsulting = new HashSet<>();
        listConsulting.add(consulting);
        Room room = new Room();
        room.setNumberRoom(1);
        room.setName("Room 1");
        room.setPresentations(listPresentation);
        room.setConsultings(listConsulting);

        // Perform assertions to validate the created Room object
        assertEquals(1, room.getNumberRoom());
        assertEquals("Room 1", room.getName());

        assertEquals(listPresentation, room.getPresentations());
        assertEquals(1, room.getPresentations().size());

        assertEquals(listConsulting, room.getConsultings());
        assertEquals(1, room.getConsultings().size());
    }

}
