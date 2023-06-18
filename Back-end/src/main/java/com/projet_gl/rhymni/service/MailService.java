package com.projet_gl.rhymni.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.projet_gl.rhymni.security.MailServiceConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

  private final MailServiceConfig mailServiceConfig;

        public void sendMailForTeacher(
          String to, int idTeam, LocalDateTime date) {
            JavaMailSender emailSender = mailServiceConfig.getJavaMailSender();
            SimpleMailMessage emailMessage = new SimpleMailMessage(); 
            emailMessage.setFrom("rhymni.projetgl@gmail.com");
            String message = "Vous avez reçu une demande de consulting de la part de l'équipe " + idTeam
                              +".\nLe consulting se déroulera le "+ formatDate(date)+ "pour une durée de 20 minutes"
                              + ".\nPour plus d'informations, veuillez cliquer sur le lien : http://localhost:5173/consulting \n\n"
                              + "Cordialement,\nL'équipe Rhymni.";
            String subject = "Consulting demandé";
            emailMessage.setTo(to);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            emailSender.send(emailMessage);
        }

        public void sendMailForTeamMember(
          String to, LocalDateTime date) {
            JavaMailSender emailSender = mailServiceConfig.getJavaMailSender();
            SimpleMailMessage emailMessage = new SimpleMailMessage(); 
            emailMessage.setFrom("rhymni.projetgl@gmail.com");
            String message = "Votre demande de consultation a bien été accepté"
                              +".\nLe consulting se déroulera le "+ formatDate(date)+ " pour une durée de 20 minutes"
                              + ".\nPour plus d'informations, veuillez cliquer sur le lien : http://localhost:5173/consulting \n\n"
                              + "Cordialement,\nL'équipe Rhymni.";
            String subject = "Consulting accepté";
            emailMessage.setTo(to);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);
            emailSender.send(emailMessage);
        }

        public String formatDate(LocalDateTime date)
        {
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy 'à' HH'h'mm", Locale.FRANCE);
          return date.format(formatter);
        }
}
 