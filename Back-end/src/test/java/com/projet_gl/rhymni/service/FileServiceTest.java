package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.projet_gl.rhymni.controller.FileController;
import com.projet_gl.rhymni.dto.FeedbackResponse;
import com.projet_gl.rhymni.dto.FileCodeResponse;
import com.projet_gl.rhymni.dto.FileUploadResponse;
import com.projet_gl.rhymni.entity.Feedback;
import com.projet_gl.rhymni.entity.File;
import com.projet_gl.rhymni.repository.FeedbackRepository;
import com.projet_gl.rhymni.repository.FileRepository;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private static final String TEST_DIRECTORY = "test-files";

    @BeforeEach
    public void setUp() {
        // Ensure test files directory exists
        Path uploadPath = Paths.get(TEST_DIRECTORY);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testGetFileCode() {
        String fileName = "example.txt";
        File file = new File();
        file.setCode("ABC123");
        List<File> files = Collections.singletonList(file);
        
        when(fileRepository.findAllByNameOrderByDateDesc(fileName)).thenReturn(files);
        
        FileCodeResponse response = fileService.getFileCode(fileName);
        
        assertEquals("ABC123", response.getFileCode());
        verify(fileRepository, times(1)).findAllByNameOrderByDateDesc(fileName);
        verifyNoMoreInteractions(fileRepository);
    }

    @Test
    void testUploadFile() throws IOException {
        // Arrange
        String fileName = "test-upload.pdf";
        MultipartFile multipartFile = new MockMultipartFile("file", fileName, "application/pdf", "content".getBytes());

        // Act
        ResponseEntity<FileUploadResponse> response = fileService.uploadFile(multipartFile, fileName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(fileName, response.getBody().getFileName());
        assertTrue(response.getBody().getDownloadUri().startsWith("/downloadFile/"));
    }

    @Test
    void testModifyFeedback() {
        // Arrange
        String fileCode = "12345";
        String comment = "This is a test comment";
        Feedback feedback = new Feedback(fileCode, "Old comment");
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse response = fileService.modifyFeedback(fileCode, comment);

        // Assert
        assertEquals(fileCode, response.getFileId());
        assertEquals(comment, response.getComment());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void testGetFeedback() {
        // Arrange
        String fileCode = "12345";
        Feedback feedback = new Feedback(fileCode, "Test comment");
        Optional<Feedback> optionalFeedback = Optional.of(feedback);
        when(feedbackRepository.findById(fileCode)).thenReturn(optionalFeedback);

        // Act
        FeedbackResponse response = fileService.getFeedback(fileCode);

        // Assert
        assertEquals(feedback.getComment(), response.getComment());
        verify(feedbackRepository, times(1)).findById(fileCode);
    }

    @Test
    void testGetFeedbackWithEmptyOptional() {
        // Arrange
        String fileCode = "12345";
        Optional<Feedback> optionalFeedback = Optional.empty();
        when(feedbackRepository.findById(fileCode)).thenReturn(optionalFeedback);

        // Act
        FeedbackResponse response = fileService.getFeedback(fileCode);

        // Assert
        assertEquals("", response.getComment());
        verify(feedbackRepository, times(1)).findById(fileCode);
    }
}
