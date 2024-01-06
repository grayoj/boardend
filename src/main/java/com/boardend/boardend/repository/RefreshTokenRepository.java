package com.boardend.boardend.repository;

import java.util.Optional;

import com.boardend.boardend.models.RefreshToken;
import com.boardend.boardend.models.Rider;
import com.boardend.boardend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    @Modifying
    int deleteByRider(Rider rider);
}