package org.ebanking.service;

import com.google.cloud.dialogflow.v2.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AiService {
    private final SessionsClient sessionsClient; // Utilisation directe du client
    private final String PROJECT_ID = "ebanking-mlsm";
    private final String LANGUAGE_CODE = "fr";

    @Autowired
    public AiService(SessionsClient sessionsClient) {
        this.sessionsClient = sessionsClient;
    }

    public String getAiResponse(String prompt) {
        try {
            // Génération d'un sessionId unique par conversation
            String sessionId = UUID.randomUUID().toString();
            SessionName session = SessionName.of(PROJECT_ID, sessionId);

            TextInput.Builder textInput = TextInput.newBuilder()
                    .setText(prompt)
                    .setLanguageCode(LANGUAGE_CODE);

            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

            // Journalisation complète
            System.out.println("========== DIALOGFLOW RESPONSE ==========");
            System.out.println("Session ID: " + sessionId);
            System.out.println("Prompt: " + prompt);
            System.out.println("Full Response: " + response);
            System.out.println("Fulfillment Text: " + response.getQueryResult().getFulfillmentText());
            System.out.println("Intent: " + response.getQueryResult().getIntent().getDisplayName());
            System.out.println("Confidence: " + response.getQueryResult().getIntentDetectionConfidence());
            System.out.println("=========================================");

            return response.getQueryResult().getFulfillmentText();
        } catch (Exception e) {
            System.err.println("Error in Dialogflow communication:");
            e.printStackTrace();
            return "Désolé, une erreur est survenue lors du traitement de votre demande.";
        }
    }
}