package com.projet_gl.rhymni.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.projet_gl.rhymni.security.MailServiceConfig;


@ExtendWith(SpringExtension.class)
class MailServiceTest {
    
    @Autowired
    private MailService mailService;

    @MockBean
    private MailServiceConfig mailServiceConfig;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void sendMailForTeacherTest() {
        String to = "teacher@example.com";
        int idTeam = 1;
        LocalDateTime date = LocalDateTime.now();
        String message = "Vous avez reçu une demande de consulting de la part de l'équipe " + idTeam
                        +".\nLe consulting se déroulera le "+ mailService.formatDate(date)+ "pour une durée de 20 minutes"
                        + ".\nPour plus d'informations, veuillez cliquer sur le lien : http://localhost:5173/consulting \n\n"
                        + "Cordialement,\nL'équipe Rhymni.";
        String subject = "Consulting demandé";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("rhymni.projetgl@gmail.com");
        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);


        when(mailServiceConfig.getJavaMailSender()).thenReturn(javaMailSender);

        mailService.sendMailForTeacher(to, idTeam, date);

        verify(javaMailSender).send(emailMessage);
    }

    @Test
    void sendMailForTeamMemberTest() {
        String to = "teammember@example.com";
        LocalDateTime date = LocalDateTime.now();
        String message = "Votre demande de consultation a bien été accepté"
                        +".\nLe consulting se déroulera le "+ mailService.formatDate(date)+ " pour une durée de 20 minutes"
                        + ".\nPour plus d'informations, veuillez cliquer sur le lien : http://localhost:5173/consulting \n\n"
                        + "Cordialement,\nL'équipe Rhymni.";
        String subject = "Consulting accepté";

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("rhymni.projetgl@gmail.com");
        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);


        when(mailServiceConfig.getJavaMailSender()).thenReturn(javaMailSender);

        mailService.sendMailForTeamMember(to, date);

        verify(javaMailSender).send(emailMessage);
    }

    @Test
    void formatDateTest() {
        LocalDateTime date = LocalDateTime.of(2023, 6, 7, 10, 30);
        String expectedFormattedDate = "07 juin 2023 à 10h30";

        MailService mailService = new MailService(mailServiceConfig);

        String formattedDate = mailService.formatDate(date);

        assertEquals(expectedFormattedDate, formattedDate);
    }
}
