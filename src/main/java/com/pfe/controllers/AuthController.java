package com.pfe.controllers;

import com.pfe.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
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
            return ResponseEntity.ok(Map.of("token", token, "email", email, "message", "Connexion réussie"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Email ou mot de passe incorrect"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}