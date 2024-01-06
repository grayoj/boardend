package com.boardend.boardend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boardend.boardend.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByCompanyName(String companyName);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void deleteById(Long id);

    Page<User> findAllByCacNumberContainingIgnoreCase(String query, Pageable pageable);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);
}
