package de.morent.backend.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerifyEmail(String email, String code){
        try{
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Bitte bestätige deine Registrierung");
            message.setFrom("registration@morent.com");
            String htmlContent = String.format("""
                    <h1 style=size: 50px; margin-bottom: 32px;">Bestätigungscode</h1>
                    <p style="margin-bottom: 50px;">Bitte bestätige deine E-Mail-Adresse, um die Registrierung abzuschließen.</p>
                    <a href="%s" style="background-color: #1985A1; color: #dcdcdd; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-bottom: 50px;">E-Mail bestätigen</a>
                    <p style="margin-top: 50px;">Vielen Dank, dass du dich bei uns registriert hast!</p>
                    """, code);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
