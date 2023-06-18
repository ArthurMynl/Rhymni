package com.projet_gl.rhymni.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.projet_gl.rhymni.dto.MailRequest;
import com.projet_gl.rhymni.service.MailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping(value = "/sendTeacher")
    @ResponseStatus(HttpStatus.CREATED)
    public String sendMailForTeacher(@RequestBody MailRequest mailRequest)
    {
        try {
            mailService.sendMailForTeacher(mailRequest.getTo(), mailRequest.getIdTeam(), mailRequest.getDate());
            return "E-mail envoyé avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'envoi de l'e-mail : " + e.getMessage();
        }
        
    }

    @PostMapping(value = "/sendTeamMember")
    @ResponseStatus(HttpStatus.CREATED)
    public String sendMailForTeamMember(@RequestBody MailRequest mailRequest)
    {
        try {
            mailService.sendMailForTeamMember(mailRequest.getTo(), mailRequest.getDate());
            return "E-mail envoyé avec succès.";
        } catch (Exception e) {
            return "Erreur lors de l'envoi de l'e-mail : " + e.getMessage();
        }
        
    }
    
}
