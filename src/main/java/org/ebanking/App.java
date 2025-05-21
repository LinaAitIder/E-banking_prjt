// Used for testing database Connection
//
//package org.ebanking;
//
//import org.ebanking.config.AppConfig;
//import org.ebanking.model.Client;
//import org.ebanking.dao.ClientRepository;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import java.util.Date;
//
//public class App {
//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//
//        try {
//            ClientRepository clientRepository = context.getBean(ClientRepository.class);
//
//            Client client = new Client();
//            client.setFullName("Alice Johnson");
//            client.setDateOfBirth(new Date());
//            client.setNationalId("ABC123456");
//            client.setEmail("alice.johnson@example.com");
//            client.setPassword("securePassword123");
//            client.setPhone("+1234567890");
//            client.setAddress("123 Main St");
//            client.setCity("Springfield");
//            client.setCountry("Neverland");
//            client.setTermsAccepted(true);
//            client.setWebAuthnEnabled(false);
//
//            clientRepository.save(client);
//
//            System.out.println("Client saved successfully with ID: " + client.getId());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            context.close();
//        }
//    }
//}
