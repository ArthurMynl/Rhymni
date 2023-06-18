package com.projet_gl.rhymni.controller;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.projet_gl.rhymni.dto.FeedbackRequest;
import com.projet_gl.rhymni.dto.FeedbackResponse;
import com.projet_gl.rhymni.dto.FileCodeResponse;
import com.projet_gl.rhymni.dto.FileUploadResponse;
import com.projet_gl.rhymni.service.FileService;

import lombok.RequiredArgsConstructor;

/**
 * Rest controller for file-related actions.
 */
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * Downloads a file based on its file code.
     *
     * @param fileCode A unique code representing the file.
     * @return A {@link ResponseEntity} containing the file as a {@link Resource}.
     */
    @GetMapping("/downloadFile/{fileCode}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileCode") String fileCode) {
        return fileService.downloadFile(fileCode);
    }

    /**
     * Uploads a file to the server.
     *
     * @param multipartFile The file to be uploaded as a {@link MultipartFile}.
     * @param name          The name of the file.
     * @return A {@link ResponseEntity} containing a {@link FileUploadResponse}
     *         object with the file upload details.
     * @throws IOException If there is an issue with file I/O during the upload
     *                     process.
     */
    @PostMapping("/uploadFile")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile multipartFile,
            @RequestParam("name") String name)
            throws IOException {
        return fileService.uploadFile(multipartFile, name);
    }

    /**
     * Retrieves the file code for a given file name.
     *
     * @param fileName The name of the file.
     * @return A {@link FileCodeResponse} object containing the file code.
     */
    @GetMapping("/getFileCode/{fileName}")
    @ResponseStatus(HttpStatus.OK)
    public FileCodeResponse getFileCode(@PathVariable("fileName") String fileName) {
        return fileService.getFileCode(fileName);
    }

    /**
     * Adds a comment to a file.
     *
     * @param fileCode        The file code of the file.
     * @param feedbackRequest A {@link FeedbackRequest} object containing the
     *                        comment
     *                        to be added.
     * @return A {@link FeedbackResponse} containing the file code of the file.
     */
    @PutMapping(value = "/{fileCode}/feedback")
    public FeedbackResponse modifyFeedback(@PathVariable("fileCode") String fileCode,
            @RequestBody FeedbackRequest feedbackRequest) {
        return fileService.modifyFeedback(fileCode, feedbackRequest.getComment());
    }

    @GetMapping(value = "/{fileCode}/getFeedback")
    public FeedbackResponse getFeedback(@PathVariable("fileCode") String fileCode) {
        return fileService.getFeedback(fileCode);
    }

}
