package org.example.shoppingproject.dao;

import org.example.shoppingproject.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
    Role findByName(String name);
}