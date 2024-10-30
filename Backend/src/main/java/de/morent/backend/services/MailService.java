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
                            <html>
        <body>
            <h1 style="font-size: 24px; color: #333;">Bitte bestätige deine E-Mail-Adresse</h1>
            <p style="font-size: 16px; color: #555;">
                Hallo,<br>
                bitte bestätige deine E-Mail-Adresse, um dein Morent-Kundenkonto aktiv nutzen zu können.
            </p>
            <p style="font-size: 20px; font-weight: bold;">
                Dein Bestätigungscode:<br><br>
                <span style="font-size: 36px; color: #596780;">%s</span>
            </p>
            <p style="font-size: 16px; color: #555;">
                Bitte kehre zur Seite „E-Mail-Adresse bestätigen“ in deinem Web-Browser zurück und trage dort diesen Code ein.
            </p>
            <p style="font-size: 16px; color: #555;">
                Der Bestätigungscode ist 15 Minuten gültig.
            </p>
            <p style="font-size: 16px; color: #555;">
                Mit freundlichen Grüßen,<br>
                Dein Morent-Team
            </p>
        </body>
        </html>
        """, code);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
