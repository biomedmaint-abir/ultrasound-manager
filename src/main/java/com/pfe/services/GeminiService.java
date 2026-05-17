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
                parts.add(Map.of("inline_data", Map.of("mime_type", mime, "data", request.getImage())));
            }
            String texte = (request.getMessage() != null && !request.getMessage().isEmpty())
                ? request.getMessage()
                : "Analyse cette image de documentation technique Philips. Identifie les codes d'erreur, leurs causes et les solutions. Réponds en français.";
            parts.add(Map.of("text", texte));
            Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", parts)));
            String jsonBody = mapper.writeValueAsString(body);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            JsonNode root = mapper.readTree(response.body());
            if (root.has("error")) return "Erreur Gemini : " + root.path("error").path("message").asText();
            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                return candidates.get(0).path("content").path("parts").get(0).path("text").asText("Réponse vide.");
            }
            return "Réponse inattendue.";
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public Map<String, Object> analyzeImage(String imageBase64, String mediaType) throws Exception {
        String prompt = """
            Tu es un expert en maintenance des équipements d'échographie Philips.
            Analyse cette image de documentation technique Philips et extrais UNIQUEMENT les informations visibles dans l'image.
            Réponds UNIQUEMENT avec un objet JSON valide, sans markdown, sans explication, sans backticks.
            Format exact attendu :
            {
              "code": "code erreur visible dans l'image (format ERR-XXX) ou null si absent",
              "symptomes": "symptômes décrits dans l'image",
              "causesProbables": "causes mentionnées dans l'image",
              "actionsCorrectives": "actions correctives décrites dans l'image",
              "piecesConcernees": "pièces mentionnées dans l'image ou null"
            }
            IMPORTANT : Extrais uniquement ce qui est écrit dans l'image. Ne complète pas avec des informations extérieures.
            """;

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(Map.of("inline_data", Map.of("mime_type", mediaType, "data", imageBase64)));
        parts.add(Map.of("text", prompt));

        Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", parts)));
        String jsonBody = mapper.writeValueAsString(body);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl + "?key=" + apiKey))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("=== GEMINI IMAGE ANALYSIS ===");
        System.out.println(response.body());
        System.out.println("=============================");

        JsonNode root = mapper.readTree(response.body());

        if (root.has("error")) {
            throw new Exception("Gemini API error: " + root.path("error").path("message").asText());
        }

        JsonNode candidates = root.path("candidates");
        if (!candidates.isArray() || candidates.size() == 0) {
            throw new Exception("Réponse Gemini vide");
        }

        String text = candidates.get(0).path("content").path("parts").get(0).path("text").asText("");
        String clean = text.replaceAll("```json", "").replaceAll("```", "").trim();

        JsonNode jsonResult = mapper.readTree(clean);

        Map<String, Object> result = new HashMap<>();
        result.put("code", jsonResult.path("code").isNull() ? null : jsonResult.path("code").asText());
        result.put("symptomes", jsonResult.path("symptomes").asText(""));
        result.put("causesProbables", jsonResult.path("causesProbables").asText(""));
        result.put("actionsCorrectives", jsonResult.path("actionsCorrectives").asText(""));
        result.put("piecesConcernees", jsonResult.path("piecesConcernees").isNull() ? null : jsonResult.path("piecesConcernees").asText());

        return result;
    }
}
