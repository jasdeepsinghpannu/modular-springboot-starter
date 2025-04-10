package com.space.auth.service;

import com.space.user.dto.AuthResponseDTO;
import com.space.user.dto.LoginRequestDTO;
import com.space.user.dto.RegisterRequestDTO;
import com.space.user.model.Role;
import com.space.user.model.User;
import com.space.user.repository.UserRepository;
import com.space.common.jwt.JwtService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

  private final UserRepository userRepository;
  private final JwtService jwtService;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository userRepository, JwtService jwtService) {
    this.userRepository = userRepository;
    this.jwtService = jwtService;
  }

  public AuthResponseDTO register(RegisterRequestDTO request) {
    logger.debug("Attempting to register user with email: {}", request.getEmail());

    if (userRepository.existsByEmail(request.getEmail())) {
      logger.error("Email {} already in use during registration", request.getEmail());
      throw new RuntimeException("Email already in use");
    }

    User user =
        new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), Role.USER);

    userRepository.save(user);
    logger.info("User successfully registered with email: {}", request.getEmail());

    String token = jwtService.generateToken(user.getEmail());
    logger.debug("Generated JWT for email: {}", request.getEmail());

    return new AuthResponseDTO(token);
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    logger.debug("Attempting to login user with email: {}", request.getEmail());

    Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

    if (optionalUser.isEmpty()) {
      logger.error("Login failed for email: {}. User not found.", request.getEmail());
      throw new RuntimeException("Invalid email or password");
    }

    User user = optionalUser.get();

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      logger.error("Login failed for email: {}. Invalid password.", request.getEmail());
      throw new RuntimeException("Invalid email or password");
    }

    String token = jwtService.generateToken(user.getEmail());
    logger.info("User successfully logged in with email: {}", request.getEmail());
    logger.debug("Generated JWT for email: {}", request.getEmail());

    return new AuthResponseDTO(token);
  }
}
