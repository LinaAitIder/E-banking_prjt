package org.ebanking.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class DialogFlowConfig {

    @Bean(destroyMethod = "close") // Fermeture propre quand l'application s'arrête
    public SessionsClient sessionsClient() throws IOException {
        return SessionsClient.create(sessionsSettings());
    }

    @Bean
    public SessionsSettings sessionsSettings() throws IOException {
        String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (credentialsPath == null) {
            credentialsPath = "src/main/resources/service-account.json";
        }

        InputStream serviceAccountStream = new FileInputStream(credentialsPath);
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

        return SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }
}