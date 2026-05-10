package com.pfe.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.dto.AssistantRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public String analyze(AssistantRequest request) {
        try {
            List<Map<String, Object>> parts = new ArrayList<>();

            if (request.getImage() != null && !request.getImage().isEmpty()) {
                String mime = request.getMimeType() != null ? request.getMimeType() : "image/png";
                parts.add(Map.of(
                    "inline_data", Map.of(
                        "mime_type", mime,
                        "data", request.getImage()
                    )
                ));
            }

            String texte = (request.getMessage() != null && !request.getMessage().isEmpty())
                ? request.getMessage()
                : "Analyse cette image de documentation technique Philips. " +
                  "Identifie les codes d'erreur, leurs causes et les solutions. " +
                  "Réponds en français.";

            parts.add(Map.of("text", texte));

            Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", parts))
            );

            String jsonBody = mapper.writeValueAsString(body);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            // Log la réponse brute pour debug
            System.out.println("=== GEMINI RAW RESPONSE ===");
            System.out.println(response.body());
            System.out.println("===========================");

            JsonNode root = mapper.readTree(response.body());

            // Vérifier si erreur dans la réponse
            if (root.has("error")) {
                return "Erreur Gemini : " + root.path("error").path("message").asText();
            }

            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText("Réponse vide.");
            }

            return "Réponse inattendue : " + response.body();

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}
