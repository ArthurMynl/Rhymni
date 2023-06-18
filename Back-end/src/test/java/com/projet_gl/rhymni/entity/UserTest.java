package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testCreateUser() {
        // Create a User object
        User user = new User();
        user.setIdUser(1);
        user.setName("John");
        user.setSurname("Doe");
        user.setMailAddress("john.doe@example.com");
        user.setRole(Role.ROLE_STUDENT);
        user.setPassword("password");

        // Perform assertions to validate the created User object
        assertEquals(1, user.getIdUser());
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("john.doe@example.com", user.getMailAddress());
        assertEquals(Role.ROLE_STUDENT, user.getRole());
        assertEquals("password", user.getPassword());

        assertNotNull(user.getAuthorities());
        assertEquals(1, user.getAuthorities().size());
        assertEquals(Role.ROLE_STUDENT.toString(), user.getAuthorities().iterator().next().getAuthority());

        assertEquals("john.doe@example.com", user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

}
