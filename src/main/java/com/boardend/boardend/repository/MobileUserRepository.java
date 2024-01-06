package com.boardend.boardend.repository;

import com.boardend.boardend.models.MobileUser;
import com.boardend.boardend.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface MobileUserRepository extends JpaRepository<MobileUser, Long> {
    Optional<MobileUser> findByUsernameIgnoreCase(String username);

    Optional<MobileUser> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByPhone(String phone);

    void deleteById(Long id);

    Optional<MobileUser> findByResetToken(String resetToken);

    Page<MobileUser> findAllByNameContainingIgnoreCase(String query, Pageable pageable);
}