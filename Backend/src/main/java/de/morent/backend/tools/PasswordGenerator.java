package de.morent.backend.tools;

import java.security.SecureRandom;

public class PasswordGenerator {

        private static final int length = 255;

        private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        private static final String DIGITS = "0123456789";
        private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+<>?";
        private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
        private static final SecureRandom RANDOM = new SecureRandom();

        public static String generateRandomPassword() {
            StringBuilder password = new StringBuilder(length);

            // Stellen sicher, dass mindestens eine Zahl, ein Sonderzeichen, ein Großbuchstabe und ein Kleinbuchstabe enthalten ist
            password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
            password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
            password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
            password.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));

            // Füllen den Rest des Passworts mit zufälligen Zeichen
            for (int i = 4; i < length; i++) {
                password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
            }

            // Mischen die Zeichen des Passworts, um es noch zufälliger zu machen
            return shuffleString(password.toString());
        }

        private static String shuffleString(String input) {
            char[] characters = input.toCharArray();
            for (int i = characters.length - 1; i > 0; i--) {
                int index = RANDOM.nextInt(i + 1);
                char temp = characters[i];
                characters[i] = characters[index];
                characters[index] = temp;
            }
            return new String(characters);
        }

}
