package com.projet_gl.rhymni.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.projet_gl.rhymni.repository.FeedbackRepository;

class FeedbackTest {
    private Feedback feedback;

    @BeforeEach
    public void setup() {
        feedback = new Feedback("1", "This is a comment");
    }

    @Test
    void testFeedbackGetters() {
        Feedback feedback = new Feedback("1", "This is a comment");
        assertNotNull(feedback);
        assertEquals("1", feedback.getFileId());
        assertEquals("This is a comment", feedback.getComment());
    }

    @Test
    void testFeedbackSetters() {
        feedback.setFileId("2");
        feedback.setComment("New comment");
        assertEquals("2", feedback.getFileId());
        assertEquals("New comment", feedback.getComment());
    }

    @Test
    void testGetReferenceById() {
        FeedbackRepository feedbackRepositoryMock = mock(FeedbackRepository.class);
        when(feedbackRepositoryMock.getReferenceById(anyString())).thenReturn(feedback);

        Feedback feedbackResult = feedbackRepositoryMock.getReferenceById("1");

        assertNotNull(feedbackResult);
        assertEquals("1", feedbackResult.getFileId());
        assertEquals("This is a comment", feedbackResult.getComment());
    }
}
