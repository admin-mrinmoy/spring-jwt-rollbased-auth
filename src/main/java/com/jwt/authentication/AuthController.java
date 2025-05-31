package com.jwt.authentication;

import com.jwt.authentication.User;
import com.jwt.authentication.UserRepository;
import com.jwt.authentication.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	// === 1. Sign Up ===
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody User user) {
		Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
		if (existingUser.isPresent()) {
			return ResponseEntity.badRequest().body("Username is already taken.");
		}

		// Encrypt password and set default role
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Collections.singleton("ROLE_USER"));

		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully.");
	}

	// === 2. Login ===
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginRequest) {
		// Authenticate user using Spring Security
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		// Load user roles from DB
		User user = userRepository.findByUsername(loginRequest.getUsername()).get();
		Set<String> roles = user.getRoles();

		// Generate token
		String token = jwtUtil.generateToken(user.getUsername(), roles);

		return ResponseEntity.ok(Collections.singletonMap("token", token));
	}

	// === 3. Dashboard (Protected Endpoint) ===
	@GetMapping("/dashboard")
	public ResponseEntity<?> dashboard() {
		return ResponseEntity.ok("Hello World from the dashboard! (JWT Protected)");
	}
}
