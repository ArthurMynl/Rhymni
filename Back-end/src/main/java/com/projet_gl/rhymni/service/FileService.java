package com.projet_gl.rhymni.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.projet_gl.rhymni.dto.FeedbackResponse;
import com.projet_gl.rhymni.dto.FileCodeResponse;
import com.projet_gl.rhymni.dto.FileUploadResponse;
import com.projet_gl.rhymni.entity.Feedback;
import com.projet_gl.rhymni.entity.File;
import com.projet_gl.rhymni.repository.FeedbackRepository;
import com.projet_gl.rhymni.repository.FileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private Path foundFile;
    private final FileRepository fileRepository;
    private final FeedbackRepository feedbackRepository;

    public ResponseEntity<Resource> downloadFile(String fileCode) {
        Resource resource = null;
        try {
            resource = getFileAsResource(fileCode);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }

        if (resource == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";
        log.info("File downloaded sucessfully");
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }

    public FileCodeResponse getFileCode(String fileName) {
        try {
            List<File> files = fileRepository.findAllByNameOrderByDateDesc(fileName);
            if (!files.isEmpty()) {
                File file = files.get(0);
                String fileCode = file.getCode();
                log.info("File code found");
                return FileCodeResponse.builder().fileCode(fileCode).build();
            }
        } catch (Exception e) {
            log.info("An error occurred while retrieving file code: {}", e.getMessage());
        }
        
        log.info("File code not found");
        return FileCodeResponse.builder().fileCode("").build();
    }
    
    public ResponseEntity<FileUploadResponse> uploadFile(MultipartFile multipartFile, String fileName)
            throws IOException {
        long size = multipartFile.getSize(); // in bytes
        String type = multipartFile.getContentType();
        
        if (type == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Security check
        if (!fileName.endsWith(".pdf") && !type.equals("application/pdf")) {
            log.info("security check failed");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String filecode = saveFile(fileName, multipartFile);

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(size);
        response.setDownloadUri("/downloadFile/" + filecode);

        log.info("File {} uploaded successfully", fileName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get("Back-end/Files-Upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        byte[] randomBytes = new byte[20];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        String fileCode = Base64.getUrlEncoder().encodeToString(randomBytes);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            fileRepository.save(new File(fileCode, fileName, LocalDateTime.now()));
            log.info("table updated");
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }

    private Resource getFileAsResource(String fileCode) throws IOException {
        Path dirPath = Paths.get("Back-end/Files-Upload");

        try (Stream<Path> files = Files.list(dirPath)) {
            files.forEach(file -> {
                if (file.getFileName().toString().startsWith(fileCode)) {
                    foundFile = file;
                    return;
                }
            });
        }

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }

    public FeedbackResponse modifyFeedback(String fileCode, String comment) {
        Feedback feedback = new Feedback(fileCode, comment);
        feedbackRepository.save(feedback);
        log.info("feedback saved");
        log.info("Feedback for file {} has been modified and saved", fileCode);
        return FeedbackResponse.builder()
                .fileId(feedback.getFileId())
                .comment(feedback.getComment())
                .build();
    }

    public FeedbackResponse getFeedback(String fileCode) {
        Optional<Feedback> feedback = feedbackRepository.findById(fileCode);

        if (feedback.isEmpty()) {
            return FeedbackResponse.builder().comment("").build();
        } else {
            return FeedbackResponse.builder().comment(feedback.get().getComment()).build();
        }
    }
}
