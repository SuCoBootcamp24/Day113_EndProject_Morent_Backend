package de.morent.backend.services;

import de.morent.backend.dtos.bookings.BookingResponseDto;
import de.morent.backend.dtos.bookings.DamageDto;
import de.morent.backend.dtos.bookings.HandOverConfirmationDto;
import de.morent.backend.dtos.bookings.NewDamageDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

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
            message.setSubject("Bitte bestätige deine Buchung");
            message.setFrom("drive.happy@morent.com");
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
                <li><strong>Buchungsdatum:</strong> %s</li>
                <li><strong>Buchungsnummer:</strong> %s</li>
                <li><strong>Kunde:</strong> %s %s</li>
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
                <li><strong>Anzahl der Tage:</strong> €%s</li>
                <li><strong>Gebühr von 150€ für die Rückgabe an einem anderen Standort:</strong> %s</li>
                <li><strong>Gesamtpreis:</strong> €%.2f</li>
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
                    booking.bookingDate(),
                    booking.bookingNumber(),
                    booking.userFirstName(), booking.userLastName(),
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
                    booking.totalDays(),
                    booking.dropOffDifferentStoreExtraCharge() ? "Ja" : "Nein",
                    booking.totalPrice(),
                    booking.status()
            );
            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHandOverConfirmationEmail(String email, HandOverConfirmationDto handOverData) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("Bestätigung der Fahrzeugrückgabe");
            message.setFrom("drive.happy@morent.com");

            String tankMessage = handOverData.isTankFull()
                    ? ""
                    : "<p style=\"font-size: 16px; color: #555;\">"
                    + "Da der Tank bei der Rückgabe nicht voll ist, werden die fehlenden Liter Kraftstoff "
                    + "entsprechend den vertraglich festgelegten Tarifen in Rechnung gestellt.</p>";

            String htmlContent = String.format("""
            <html>
                <body>
                    <h1 style="font-size: 24px; color: #333;">Bestätigung Ihrer Fahrzeugrückgabe</h1>
                    <p style="font-size: 16px; color: #555;">
                        Hallo %s %s,<br>
                        vielen Dank für die Rückgabe des Fahrzeugs. Hier sind die Details zur Rückgabe:
                    </p>

                    <h2 style="font-size: 20px; color: #444;">Buchungsdetails</h2>
                    <ul style="font-size: 16px; color: #555;">
                        <li><strong>Buchungsnummer:</strong> %s</li>
                        <li><strong>Standort der Rückgabe:</strong> %s, %s</li>
                        <li><strong>Rückgabedatum:</strong> %s</li>
                    </ul>

                    <h2 style="font-size: 20px; color: #444;">Fahrzeugzustand bei Rückgabe</h2>
                    <ul style="font-size: 16px; color: #555;">
                        <li><strong>Aktueller Kilometerstand:</strong> %d km</li>
                        <li><strong>Vollgetankt:</strong> %s</li>
                    </ul>

                    %s %s

                    <h2 style="font-size: 20px; color: #444;">Gesamtpreis</h2>
                    <p style="font-size: 16px; color: #555;">€%.2f</p>

                    <p style="font-size: 16px; color: #555;">
                        Sollte es neue Schäden am Fahrzeug geben, werden wir eine zusätzliche Überprüfung vornehmen 
                        und Sie über eventuelle Zusatzkosten informieren.
                    </p>

                    <p style="font-size: 16px; color: #555;">
                        Mit freundlichen Grüßen,<br>
                        Ihr Morent-Team
                    </p>
                </body>
            </html>
            """,
                    handOverData.bookingData().userFirstName(),
                    handOverData.bookingData().userLastName(),
                    handOverData.bookingData().bookingNumber(),
                    handOverData.bookingData().dropOffStoreName(),
                    handOverData.bookingData().dropOffStoreCity(),
                    handOverData.bookingData().dropOffDate(),
                    handOverData.newMileage(),
                    handOverData.isTankFull() ? "Ja" : "Nein",
                    formatDamageSection(handOverData.newDamages()),
                    tankMessage,
                    handOverData.bookingData().totalPrice()
            );

            message.setContent(htmlContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private String formatDamageSection(List<NewDamageDto> newDamages) {
        if (newDamages.isEmpty()) {
            return "<p style=\"font-size: 16px; color: #555;\">Es wurden keine neuen Schäden festgestellt.</p>";
        }

        StringBuilder damagesHtml = new StringBuilder("<h2 style=\"font-size: 20px; color: #444;\">Neue Schäden</h2><ul style=\"font-size: 16px; color: #555;\">");

        for (NewDamageDto damage : newDamages) {
            damagesHtml.append(String.format("<li><strong>Position:</strong> %s - <strong>Beschreibung:</strong> %s</li>",
                    damage.position(),
                    damage.description()));
        }
        damagesHtml.append("</ul>");

        return damagesHtml.toString();
    }
}
