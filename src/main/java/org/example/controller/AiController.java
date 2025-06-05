package org.example.controller;

import org.example.model.AiRequest;
import org.example.model.AiResponse;
import org.example.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/ask")
    public ResponseEntity<AiResponse> askAi(@RequestBody AiRequest request) {
        try {
            System.out.println("Received new request with prompt: " + request.getPrompt());

            if (request.getPrompt() == null || request.getPrompt().isEmpty()) {
                System.out.println("Empty prompt received");
                return ResponseEntity.badRequest()
                        .body(new AiResponse("Le prompt est requis"));
            }

            String response = aiService.getAiResponse(request.getPrompt());
            return ResponseEntity.ok().body(new AiResponse(response));
        } catch (Exception e) {
            System.err.println("Error processing AI request:");
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(new AiResponse("Erreur interne du serveur"));
        }
    }
}