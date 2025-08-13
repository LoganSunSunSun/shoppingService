package org.example.shoppingproject.service;


import org.example.shoppingproject.dao.UserRepository;
import org.example.shoppingproject.domain.User;
import org.example.shoppingproject.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring passes the 'username' field; we'll accept username or email
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//        User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
//        System.out.println(user.getUsername() + " " + user.getPasswordHash());
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username/email: " + usernameOrEmail);
//        }
//        return new CustomUserDetails(user);

        try {
            User user = userRepository.findByUsernameOrEmail(usernameOrEmail);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username/email: " + usernameOrEmail);
            }
            return new CustomUserDetails(user);
        } catch (Exception e) {
            System.err.println("Exception in loadUserByUsername: " + e.getMessage());
            e.printStackTrace();
            throw e;  // rethrow to let Spring handle it
        }
    }
}