package org.ebanking.util;

public class GenerateJwtSecret {
    public static void main(String[] args) {
        String secret = JwtUtil.generateSecureSecretKey();
        System.out.println("Ajoutez cette clé à votre .env :");
        System.out.println("JWT_SECRET=" + secret);
    }
}