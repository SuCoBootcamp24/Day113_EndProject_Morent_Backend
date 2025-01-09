# Morent Backend

Willkommen im Backend-teil des **Morent-Projekts**! Dieses Projekt ist das Abschluss-Projekt unseres Java-Backend-Entwicklungs-Bootcamps. Es wurde in Teamarbeit innerhalb von 10 Tagen basierend auf einer kurzen Themenvorlage entwickelt.
Die **Morent-Website** ermöglicht es Benutzern, Autos an verschiedenen Standorten zu mieten, an denen eine Filiale von Morent existiert.

Das **Morent Backend** bildet die Grundlage für die Verwaltung von Buchungen und Fahrzeugbildern sowie für die Nutzer-Authentifizierung und die filterbasierte Suche nach Fahrzeugen und Standorten auf der Website.


**Frontend-Repository**: [Morent Frontend](https://github.com/SuCoBootcamp24/Day113_EndProject_Morent_Frontend)

## 📦 Projektübersicht

Das Morent Backend ist ein RESTful API-Service auf Basis von **Spring Boot** und bietet folgende Features:

1. **Email-Verifikation**: Nach der Registrierung erhält der Benutzer eine Bestätigungs-E-Mail. Erst nach der Bestätigung wird der Account freigeschaltet und der Benutzer kann die Plattform nutzen.

2. **Erweiterte Storesuche**: Neben der einfachen Suche nach Städten ermöglicht das System automatisch eine Standort-basierte Suche und zeigt die 5 nächstgelegenen Stores zum angegebenen Ort an. Diese Funktion hilft Nutzern, schnell eine Filiale in ihrer Nähe zu finden.

3. **Suche und Filterung**: Die API unterstützt eine kombinierte Such- und Filterfunktion, sodass Benutzer nach spezifischen Kriterien suchen und Ergebnisse nach Belieben eingrenzen können.

4. **Favoritenfunktion**: Nutzer können Autos zu ihren Favoriten hinzufügen, um schnellen Zugriff auf bevorzugte Autos zu haben.

5. **Newsletter (Vorbereitung)**: Ein Newsletter-System ist vorbereitet, um zukünftig die Benutzer mit aktuellen Informationen zu versorgen. Das Feature inkludiert eine Unterscheidung, ob der Benutzer registriert ist oder nicht.

6. **Admin-Panel**: Ein exklusiver Bereich für Mitarbeiter, der zusätzliche Features bietet, um Benutzerdaten, Buchungen und Rückgaben zu verwalten und den Systemstatus zu überwachen.

7. **Buchungssystem**: Ermöglicht es den Nutzern, Reservierungen für Fahrzeuge zu erstellen und Details zu verwalten.

8. **Rückgabesystem**: Nach einer Buchung können Nutzer Fahrzeuge zurückgeben, und Rückgabedetails werden systematisch erfasst.

9. **Buchungsübersicht**: Die Benutzer haben eine klare Übersicht über ihre aktuellen und vergangenen Buchungen.

10. **Bild-Upload**: Benutzer können Bilder hochladen, welche dann mit Fahrzeug- und Benutzerprofilen verknüpft werden.

11. **Sicherheitsfilter**: Das Backend enthält eine Sicherheitsvorkehrung, die nur eine begrenzte Anzahl an Anfragen pro Zeiteinheit von derselben IP-Adresse zulässt, um Missbrauch und DDoS-Angriffe zu verhindern.

12. **Adressüberprüfung**: Bei der Eingabe einer Adresse (für Stores und Benutzer) wird im Hintergrund geprüft, ob es sich um eine korrekte und existierende Adresse handelt.

## 🚀 Technologien

- **Java 21**
- **Spring Boot**
- **oauth2**
- **Docker** mit **Docker Compose**
- **Redis** für Caching und Geo-Berechnungen
- **PostgreSQL** als Datenbank
- **JwtEncoder** für Token-Authentifizierung
- **Imgbb** für Bild-Upload (externer Dienst)
- **Mailtrap** für das testen der e-mail Versendung (externer Dienst)

## 🎥 Projektpräsentation
Wir haben hier ein kleines Demovideo erstellt, um zu zeigen wie unser Projekt mit inkl. den Features aussieht.
die Fahrzeug bilder sind Muster und können mit Adminrechte aktualisiert werden.

[![Videotitel](https://img.youtube.com/vi/sj8I2FK4zo4/0.jpg)](https://www.youtube.com/watch?v=sj8I2FK4zo4)

## 👥 Team
Das Team bestand aus 3 Personen:

- [Andreas Reich](https://github.com/andreasReichHl)
- [Vittorio De Marzi](https://github.com/VittorioDeMarzi)
- [René Behrens](https://github.com/ReBehrens)

## 📌 API-Endpunkte

Hier sind einige der wichtigsten API-Endpunkte, die die Kernfunktionen des Projekts darstellen:

#### Benutzer-Authentifizierung:
- **Registrierung**: `/api/v1/auth/signUp` – Benutzerregistrierung
- **Email-Verifikation**: `/api/v1/auth/unlock` – Bestätigt die E-Mail des Benutzers
- **Login**: `/api/v1/auth/login` – Authentifizierung

#### Suche und Filterung:
- **Suche nach Stadt**: `/api/v1/stores/geosearch?city={city}` – Suche nach Filialen in einer Stadt, zeigt zusätzlich die 5 nächstgelegenen Filialen.
- **Suche nach Autos**: `/api/v1/vehicles/exemplars` – Suche nach Fahrzeugen in der Gesuchten Filiale, hier kann zusätzliche nach weiteren Kriterien gefiltert werden.

#### Buchung und Rückgabe:
- **Buchung erstellen**: `/api/v1/booking` – Erstellt eine Buchung
- **Rückgabe**: `/api/v1/handover/return` – Abschluss einer Fahrzeugrückgabe

#### Favoritenfunktion:
- **Favoriten hinzufügen**: `/api/v1/favorite` – Fügt eine Filiale zu den Favoriten hinzu

#### Bild-Upload:
- **Bild hochladen**: `/api/v1/images/user` – Ermöglicht das Hochladen von Profilbildern der Benutzer.
- **Bild hochladen**: `/api/v1/images/vehicle` – Ermöglicht das Hochladen von Bildern für Fahrzeuge.

Diese Endpunkte decken die Hauptfunktionen des Projekts ab.


## 🔧 Installation

1. **Projekt klonen**:
    ```bash
    git clone https://github.com/SuCoBootcamp24/Day113_EndProject_Morent_Backend.git
    cd Day113_EndProject_Morent_Backend
    ```

2. **Abhängigkeiten installieren**:
    ```bash
    mvn install
    ```

3. **Datenbank konfigurieren**: Passe die Einstellungen in den `.env`-Dateien an.

## 🐳 Docker-Setup

### Entwicklungsumgebung:
```bash
docker-compose -f docker-compose.yml up --build
```

###Produktionsumgebung:
```bash
docker-compose -f docker-compose-depl.yml up --build
```

Hinweis: Passe die `.env-Dateien entsprechend der Umgebung an.



##🔒 Umgebungseinstellungen

Verwende `.env.dev` für die Entwicklung und `.env.prod` für die Produktion. Diese Dateien enthalten Umgebungsvariablen für die Datenbank, Redis und externe Dienste.
