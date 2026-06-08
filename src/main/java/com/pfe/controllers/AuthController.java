package com.pfe.controllers;

import com.pfe.entities.Utilisateur;
import com.pfe.repositories.UtilisateurRepository;
import com.pfe.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/encode/{password}")
    public String encodePassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = jwtUtil.generateToken(email);

            String role = utilisateurRepository.findByEmail(email)
                .map(u -> u.getRole() != null ? u.getRole().getNom() : "INGENIEUR")
                .orElse("INGENIEUR");

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("role", role);
            Utilisateur u = utilisateurRepository.findByEmail(email).orElse(null);
            response.put("nom", u != null && u.getNom() != null ? u.getNom() : "");
            response.put("prenom", u != null && u.getPrenom() != null ? u.getPrenom() : "");
            response.put("id", u != null ? String.valueOf(u.getId()) : "");
            response.put("message", "Connexion reussie");
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Deconnexion reussie"));
    }
}
