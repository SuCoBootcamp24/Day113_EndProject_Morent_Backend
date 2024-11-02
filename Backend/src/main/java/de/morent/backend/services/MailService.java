package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingResponseDto;
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

    public void sendBookingConfirmationEmail(String email, BookingResponseDto booking) {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Bitte bestätige deine Registrierung");
            message.setFrom("registration@morent.com");
            String htmlContent = String.format("""
    <html>
        <body>
            <h1 style="font-size: 24px; color: #333;">Ihre Buchungsbestätigung</h1>
            <p style="font-size: 16px; color: #555;">
                Hallo %s %s,<br>
                vielen Dank für Ihre Buchung! Hier sind die Details zu Ihrer Buchung:
            </p>

            <h2 style="font-size: 20px; color: #444;">Buchungsdetails</h2>
            <ul style="font-size: 16px; color: #555;">
                <li><strong>Buchungsnummer:</strong> %s</li>
                <li><strong>Buchungsdatum:</strong> %s</li>
                <li><strong>Geburtsdatum:</strong> %s</li>
            </ul>

            <h2 style="font-size: 20px; color: #444;">Fahrzeugdetails</h2>
            <ul style="font-size: 16px; color: #555;">
                <li><strong>Fahrzeugtyp:</strong> %s</li>
                <li><strong>Marke:</strong> %s</li>
                <li><strong>Modell:</strong> %s</li>
                <li><strong>Sitze:</strong> %d</li>
                <li><strong>Motorgröße:</strong> %.1f L</li>
                <li><strong>Automatik:</strong> %s</li>
                <li><strong>Kraftstoffart:</strong> %s</li>
                <li><strong>Verbrauch:</strong> %.1f L/100km</li>
            </ul>

            <h2 style="font-size: 20px; color: #444;">Abhol- und Rückgabedetails</h2>
            <ul style="font-size: 16px; color: #555;">
                <li><strong>Abholdatum:</strong> %s</li>
                <li><strong>Geplantes Rückgabedatum:</strong> %s</li>
                <li><strong>Abholort:</strong> %s, %s, %s %s, %s</li>
                <li><strong>Rückgabeort:</strong> %s, %s, %s %s, %s</li>
            </ul>

            <h2 style="font-size: 20px; color: #444;">Preisinformationen</h2>
            <ul style="font-size: 16px; color: #555;">
                <li><strong>Preis pro Tag:</strong> €%.2f</li>
                <li><strong>Gesamtpreis:</strong> €%.2f</li>
                <li><strong>Rückgabe an einem anderen Standort:</strong> %s</li>
            </ul>

            <p style="font-size: 16px; color: #555;">
                Ihr Status: <strong>%s</strong><br>
                Bei Fragen oder Änderungen zu Ihrer Buchung kontaktieren Sie bitte unseren Kundenservice.
            </p>

            <p style="font-size: 16px; color: #555;">
                Mit freundlichen Grüßen,<br>
                Ihr Morent-Team
            </p>
        </body>
    </html>
    """,
                    booking.userFirstName(), booking.userLastName(),
                    booking.bookingNumber(),
                    booking.bookingDate(),
                    booking.dateOfBirth(),
                    booking.vehicle().carType(),
                    booking.vehicle().brand(),
                    booking.vehicle().model(),
                    booking.vehicle().seats(),
                    booking.vehicle().engineCapacity() / 1000.0,
                    booking.vehicle().isAutomatic() ? "Ja" : "Nein",
                    booking.vehicle().fuelType(),
                    booking.vehicle().consumption(),
                    booking.pickUpDate(),
                    booking.plannedDropOffDate(),
                    booking.pickUpLocation().name(),
                    booking.pickUpLocation().address().street() + " " + booking.pickUpLocation().address().houseNumber(),
                    booking.pickUpLocation().address().zipCode(),
                    booking.pickUpLocation().address().city(),
                    booking.pickUpLocation().address().country(),
                    booking.dropOffLocation().name(),
                    booking.dropOffLocation().address().street() + " " + booking.dropOffLocation().address().houseNumber(),
                    booking.dropOffLocation().address().zipCode(),
                    booking.dropOffLocation().address().city(),
                    booking.dropOffLocation().address().country(),
                    booking.pricePerDay(),
                    booking.totalPrice(),
                    booking.dropOffDifferentStoreExtraCharge() ? "Ja" : "Nein",
                    booking.status()
            );
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
