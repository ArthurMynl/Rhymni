package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlanningAssistantTest {

    @Test
    void testCreatePlanningAssistant() {
        // Create a PlanningAssistant object
        PlanningAssistant planningAssistant = new PlanningAssistant();
        planningAssistant.setIdUser(1);
        planningAssistant.setName("John");
        planningAssistant.setSurname("Doe");
        planningAssistant.setMailAddress("john.doe@example.com");
        planningAssistant.setPassword("password123");
        planningAssistant.setRole(Role.ROLE_PLANNING_ASSISTANT);

        // Perform assertions to validate the created PlanningAssistant object
        assertEquals(1, planningAssistant.getIdUser());
        assertEquals("John", planningAssistant.getName());
        assertEquals("Doe", planningAssistant.getSurname());
        assertEquals("john.doe@example.com", planningAssistant.getMailAddress());
        assertEquals(Role.ROLE_PLANNING_ASSISTANT, planningAssistant.getRole());
        assertEquals("password123", planningAssistant.getPassword());

        // Additional assertions for UserDetails interface methods
        assertNotNull(planningAssistant.getAuthorities());
        assertEquals(1, planningAssistant.getAuthorities().size());
        assertEquals("ROLE_PLANNING_ASSISTANT", planningAssistant.getAuthorities().iterator().next().getAuthority());
        assertEquals("john.doe@example.com", planningAssistant.getUsername());
        assertTrue(planningAssistant.isAccountNonExpired());
        assertTrue(planningAssistant.isAccountNonLocked());
        assertTrue(planningAssistant.isCredentialsNonExpired());
        assertTrue(planningAssistant.isEnabled());
    }
}
