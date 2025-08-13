package org.example.shoppingproject.controller;


import org.example.shoppingproject.dao.RoleRepository;
import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.domain.Role;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.dto.LoginRequest;
import org.example.shoppingproject.dto.LoginResponse;
import org.example.shoppingproject.dto.RegisterRequest;
import org.example.shoppingproject.exception.InvalidCredentialsException;
import org.example.shoppingproject.security.CustomUserDetails;
import org.example.shoppingproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider tokenProvider,
                          PasswordEncoder passwordEncoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.findByUsername(req.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already taken");
        }
        if (userRepository.findByEmail(req.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            return ResponseEntity.status(500).body("Default USER role not configured");
        }
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);

        return ResponseEntity.status(201).body("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        // manual testing
//        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail());
//        if (user != null) {
//            boolean matches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
//            System.out.println("Password match: " + matches);
//        } else {
//            System.out.println("User not found for password match test");
//        }


        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
            );

            String jwt = tokenProvider.createToken((UserDetails) authentication.getPrincipal());

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toSet());

            LoginResponse resp = new LoginResponse();
            resp.setToken(jwt);
            resp.setUsername(userDetails.getUsername());
            resp.setRoles(roles);

            return ResponseEntity.ok(resp);
    } catch (BadCredentialsException ex) {
        throw new InvalidCredentialsException(); // handled by your controller advice
    }
    }
}