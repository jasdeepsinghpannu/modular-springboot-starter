package com.space.auth.controller;

import com.space.auth.service.AuthService;
import com.space.user.dto.AuthResponseDTO;
import com.space.user.dto.LoginRequestDTO;
import com.space.user.dto.RegisterRequestDTO;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO request) {
    return ResponseEntity.ok(authService.register(request));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
    return ResponseEntity.ok(authService.login(request));
  }

  @GetMapping("/check")
  public ResponseEntity<?> checkAuth(Authentication authentication) {
    if (authentication == null) {
      return ResponseEntity.status(401).body("Not authenticated");
    }

    return ResponseEntity.ok(Map.of("email", authentication.getName(), "status", "authenticated"));
  }
}
