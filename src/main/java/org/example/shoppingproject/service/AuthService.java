package org.example.shoppingproject.service;

import lombok.RequiredArgsConstructor;
import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.domain.Role;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.dto.LoginRequest;
import org.example.shoppingproject.dto.RegisterRequest;
import org.example.shoppingproject.exception.AccessDeniedException;
import org.example.shoppingproject.security.CustomUserDetails;
import org.example.shoppingproject.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Returns the current authenticated user entity from the database.
     * Throws AccessDeniedException if no authenticated user.
     */
    public User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User not authenticated");
        }

        String username = authentication.getName(); // from CustomUserDetails#getUsername()
        return userRepository.findByUsername(username);
    }

    /**
     * Returns the current CustomUserDetails directly.
     */
    public CustomUserDetails getCurrentUserDetails() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        throw new AccessDeniedException("User not authenticated");
    }
}

