package de.morent.backend.converter;

import org.springframework.stereotype.Component;


public class VowelConverter {

    public static String convertString(String input) {
        return input
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue")
                .replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("ß", "ss");
    }
}
