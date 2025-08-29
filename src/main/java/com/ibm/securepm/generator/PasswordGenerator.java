package com.ibm.securepm.generator;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:,.<>?";

    public static String generatePassword(int length, boolean useLower, boolean useUpper, 
                                       boolean useDigits, boolean useSpecial) {
        StringBuilder password = new StringBuilder(length);
        String charPool = "";
        SecureRandom random = new SecureRandom();

        if (useLower) charPool += LOWER;
        if (useUpper) charPool += UPPER;
        if (useDigits) charPool += DIGITS;
        if (useSpecial) charPool += SPECIAL;

        // Ensure at least one character from each selected type
        if (useLower) password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        if (useUpper) password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        if (useDigits) password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        if (useSpecial) password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // Fill the rest of the password
        for (int i = password.length(); i < length; i++) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }

        // Shuffle the password
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
}
