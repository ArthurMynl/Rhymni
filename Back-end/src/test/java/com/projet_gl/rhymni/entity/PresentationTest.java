package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class PresentationTest {

    @Test
    void testCreatePresentation() {
        // Create a Room object for the presentation
        Room room = new Room();
        room.setNumberRoom(1);

        // Create a Presentation object
        Presentation presentation = Presentation.builder()
                .idPresentation(1)
                .dateHours(LocalDateTime.now())
                .type("Présentation Intermédiaire")
                .room(room)
                .build();

        // Perform assertions to validate the created Presentation object
        assertEquals(1, presentation.getIdPresentation());
        assertNotNull(presentation.getDateHours());
        assertEquals("Présentation Intermédiaire", presentation.getType());
        assertEquals(room, presentation.getRoom());
    }

}
