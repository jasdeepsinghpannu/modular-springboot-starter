package com.space.auth.service;

import com.space.user.dto.AuthResponseDTO;
import com.space.user.dto.LoginRequestDTO;
import com.space.user.dto.RegisterRequestDTO;
import com.space.user.model.Role;
import com.space.user.model.User;
import com.space.user.repository.UserRepository;
import com.space.common.jwt.JwtService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  public AuthResponseDTO register(RegisterRequestDTO request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already in use");
    }

    User user =
        new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.USER);

    userRepository.save(user);

    String token = jwtService.generateToken(user.getEmail());
    return new AuthResponseDTO(token);
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty()) {
      throw new RuntimeException("Invalid email or password");
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid email or password");
    }

    String token = jwtService.generateToken(user.getEmail());
    return new AuthResponseDTO(token);
  }
}
