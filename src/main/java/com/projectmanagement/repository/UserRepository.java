package com.projectmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectmanagement.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);
}
