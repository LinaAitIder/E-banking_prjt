package org.ebanking.model;

import java.util.Random;

public class RibGenerator {

    public static String generateRib() {
        String bankCode = "12345";
        String branchCode = "67890";

        String accountNumber = String.format("%011d", Math.abs(new Random().nextLong()) % 1_000_000_00000L);
        String key = String.format("%02d", new Random().nextInt(90) + 10);

        return bankCode + branchCode + accountNumber + key;
    }
}
