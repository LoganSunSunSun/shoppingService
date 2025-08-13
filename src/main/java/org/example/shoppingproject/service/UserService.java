package org.example.shoppingproject.service;

import org.example.shoppingproject.dao.RoleRepository;
import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.domain.Role;
import org.example.shoppingproject.domain.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = userRepository.findById(userId);
//                .orElseThrow(() -> new RuntimeException("User not found"));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
//                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        user.getRoles().add(adminRole);
        userRepository.save(user); // persist change
    }
}
