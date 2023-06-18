package com.projet_gl.rhymni.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.projet_gl.rhymni.dto.FileCodeResponse;
import com.projet_gl.rhymni.dto.FileUploadResponse;
import com.projet_gl.rhymni.service.FileService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @AfterEach
    void tearDown() {
        reset(fileService);
    }

    @Test
    @WithMockUser
    void testDownloadFile() throws Exception {
        String fileCode = "randomFileCode";
        Resource resource = new ByteArrayResource("Test content".getBytes());

        ResponseEntity<Resource> responseEntity = ResponseEntity.ok().body(resource);
        when(fileService.downloadFile(fileCode)).thenReturn(responseEntity);

        mockMvc.perform(get("/file/downloadFile/{fileCode}", fileCode))
                .andExpect(status().isOk())
                .andExpect(content().string("Test content"));

        verify(fileService, times(1)).downloadFile(fileCode);
    }

    @Test
    @WithMockUser
    void testUploadFile() throws Exception {
        String fileName = "test.pdf";
        String fileCode = "randomFileCode";
        MockMultipartFile multipartFile = new MockMultipartFile("file", fileName, "application/pdf",
                "Test content".getBytes());

        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSize(multipartFile.getSize());
        response.setDownloadUri("/downloadFile/" + fileCode);

        when(fileService.uploadFile(multipartFile, fileName)).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(multipart("/file/uploadFile").file(multipartFile).param("name", fileName))
                .andExpect(status().isOk());
        verify(fileService, times(1)).uploadFile(multipartFile, fileName);
    }

    @Test
    @WithMockUser
    void testGetFileCode() throws Exception {
        String fileName = "test.pdf";
        String fileCode = "randomFileCode";

        when(fileService.getFileCode(fileName)).thenReturn(FileCodeResponse.builder().fileCode(fileCode).build());

        mockMvc.perform(get("/file/getFileCode/{fileName}", fileName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileCode", is(fileCode)));

        verify(fileService, times(1)).getFileCode(fileName);
    }
}
