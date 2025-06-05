package org.example;
import io.github.cdimascio.dotenv.Dotenv;
import org.example.config.DialogFlowConfig;
import org.example.service.AiService;


import java.io.IOException;
import java.sql.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        System.out.println( "Hello World!" );

        Dotenv dotenv = Dotenv.load(); // Charge le fichier .env
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ Connecté à Neon.tech !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur : " + e.getMessage());
        }


    }
}
