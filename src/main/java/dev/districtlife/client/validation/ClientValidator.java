package dev.districtlife.client.validation;

import java.util.regex.Pattern;

public class ClientValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\-']+(?: [\\p{L}\\-']+)?$");
    private static final int NAME_MIN = 3;
    private static final int NAME_MAX = 16;
    private static final int AGE_MIN = 18;
    private static final int AGE_MAX = 90;

    public static boolean validateFirstName(String value) {
        return validateName(value);
    }

    public static boolean validateLastName(String value) {
        return validateName(value);
    }

    private static boolean validateName(String value) {
        if (value == null || value.length() < NAME_MIN || value.length() > NAME_MAX) return false;
        return NAME_PATTERN.matcher(value).matches();
    }

    /**
     * Validation de la date côté client (UX uniquement).
     * rpYear = année RP courante pour le calcul d'âge.
     */
    public static boolean validateBirthDate(int day, int month, int year, int rpYear) {
        if (month < 1 || month > 12) return false;
        if (day < 1 || day > 31) return false;
        if (year < 1900 || year > 9999) return false;

        // Vérification sommaire du nombre de jours
        int[] maxDays = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (day > maxDays[month]) return false;

        int age = rpYear - year;
        return age >= AGE_MIN && age <= AGE_MAX;
    }

    /**
     * Capitalise chaque mot : première lettre majuscule, reste minuscules.
     */
    public static String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] parts = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        String result = sb.toString().trim();
        // Préserver l'espace en fin si l'utilisateur est en train de taper
        if (input.endsWith(" ") && !result.endsWith(" ")) result += " ";
        return result;
    }
}
