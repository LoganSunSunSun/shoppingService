package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.User;

public interface UserRepository {
    User findByUsername(String username);
    User findByEmail(String email);
    User findByUsernameOrEmail(String usernameOrEmail);
    User save(User user); // simple persist
}
