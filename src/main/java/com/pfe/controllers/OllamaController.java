package com.pfe.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.dto.AnalyzeImageRequest;
import com.pfe.dto.PhilipsErrorAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
public class OllamaController {

    private static final String MODEL = "llava";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ollama.api.url}")
    private String ollamaApiUrl;

    /** Optional; required for Ollama Cloud (Bearer token). Local Ollama ignores it. */
    @Value("${ollama.api.key:}")
    private String ollamaApiKey;

    public OllamaController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/analyze-image")
    public ResponseEntity<?> analyzeImage(@RequestBody AnalyzeImageRequest request) {
        String base64 = request.getImageBase64();
        if (base64 == null || base64.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "imageBase64 est requis."));
        }
        int comma = base64.indexOf(',');
        if (base64.startsWith("data:") && comma > 0) {
            base64 = base64.substring(comma + 1);
        }
        base64 = base64.trim();

        String prompt = """
            Tu analyses une image de document d'erreur ou de maintenance pour équipement d'échographie médicale Philips.
            Réponds UNIQUEMENT avec un objet JSON valide, sans aucune explication, sans markdown, sans backticks, sans texte avant ou après.
            Le JSON doit avoir exactement ces clés et types :
            "code" : chaîne au format ERR-XXX si un code d'erreur est visible, sinon la valeur null ;
            "symptomes" : symptômes décrits dans le document ;
            "causesProbables" : causes probables ;
            "actionsCorrectives" : actions correctives à mener ;
            "piecesConcernees" : pièces détachées mentionnées, sinon null.
            Toutes les chaînes de texte en français. Utilise null (JSON) là où l'information est absente.
            """;

        String base = ollamaApiUrl == null ? "" : ollamaApiUrl.trim();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String generateUrl = base + "/api/generate";

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", MODEL);
        body.put("prompt", prompt);
        body.put("images", List.of(base64));
        body.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (ollamaApiKey != null && !ollamaApiKey.isBlank()) {
            headers.setBearerAuth(ollamaApiKey.trim());
        }
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> ollamaResponse = restTemplate.exchange(
                generateUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            if (!ollamaResponse.getStatusCode().is2xxSuccessful() || ollamaResponse.getBody() == null) {
                return ResponseEntity.status(502).body(Map.of("error", "Réponse Ollama invalide."));
            }

            JsonNode root = objectMapper.readTree(ollamaResponse.getBody());
            String rawText = root.path("response").asText(null);
            if (rawText == null || rawText.isBlank()) {
                return ResponseEntity.status(500).body(Map.of("error", "Réponse Ollama vide ou sans champ response."));
            }

            PhilipsErrorAnalysisResponse parsed = parseModelJson(rawText);
            return ResponseEntity.ok(parsed);
        } catch (RestClientException e) {
            return ResponseEntity.status(503).body(Map.of("error", "Impossible de joindre Ollama : " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Analyse JSON impossible : " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erreur lors de l'analyse : " + e.getMessage()));
        }
    }

    private PhilipsErrorAnalysisResponse parseModelJson(String text) throws Exception {
        String json = text.trim();
        if (json.startsWith("```")) {
            json = json.replaceFirst("^```(?:json)?\\s*", "");
            json = json.replaceFirst("\\s*```\\s*$", "");
            json = json.trim();
        }
        int start = json.indexOf('{');
        int end = json.lastIndexOf('}');
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("Aucun objet JSON trouvé dans la sortie du modèle.");
        }
        json = json.substring(start, end + 1);

        PhilipsErrorAnalysisResponse out = objectMapper.readValue(json, PhilipsErrorAnalysisResponse.class);
        if (out == null) {
            throw new IllegalArgumentException("JSON analysé vide.");
        }
        return out;
    }
}
