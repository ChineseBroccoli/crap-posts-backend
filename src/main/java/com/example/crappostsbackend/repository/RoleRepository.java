package com.example.crappostsbackend.repository;

import com.example.crappostsbackend.enums.ERole;
import com.example.crappostsbackend.model.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(ERole role);
}
