package com.pfe.controllers;

import com.pfe.dto.AssistantRequest;
import com.pfe.services.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    private final GeminiService geminiService;

    public AssistantController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody AssistantRequest request) {
        String response = geminiService.analyze(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/analyze-image")
    public ResponseEntity<?> analyzeImage(@RequestBody Map<String, String> body) {
        try {
            String imageBase64 = body.get("imageBase64");
            String mediaType = body.getOrDefault("mediaType", "image/jpeg");
            if (imageBase64 == null || imageBase64.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Image manquante"));
            }
            Map<String, Object> result = geminiService.analyzeImage(imageBase64, mediaType);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
