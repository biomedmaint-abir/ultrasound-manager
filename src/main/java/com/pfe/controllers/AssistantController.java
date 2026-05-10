package com.pfe.controllers;

import com.pfe.dto.AssistantRequest;
import com.pfe.services.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
