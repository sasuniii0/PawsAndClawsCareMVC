package lk.ijse.gdse.pawsandclawscaremvc.util.security;

import java.util.Random;

public class VerificationCodeGenerator {
    private static final String NUMBERS = "0123456789";

    public String getCode(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random(); // Create a single Random instance

        for (int i = 0; i < length; i++) {
            char selectedNumber = NUMBERS.charAt(random.nextInt(10));
            if (i == 0 && selectedNumber == '0') {
                // Avoid leading zeros
                selectedNumber = NUMBERS.charAt(random.nextInt(9) + 1); // Select from '1' to '9'
            }
            sb.append(selectedNumber);
        }

        return sb.toString(); // Return as a String
    }
}
