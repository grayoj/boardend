package com.boardend.boardend.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.boardend.boardend.exception.TokenRefreshException;
import com.boardend.boardend.models.RefreshToken;
import com.boardend.boardend.repository.MobileUserRepository;
import com.boardend.boardend.repository.RefreshTokenRepository;
import com.boardend.boardend.repository.RiderRepository;
import com.boardend.boardend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    @Value("${logistics.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MobileUserRepository mobileUserRepository;
    @Autowired
    private RiderRepository riderRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshTokenForUser(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken createRefreshTokenForRider(Long riderId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setRider(riderRepository.findById(riderId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken createRefreshTokenForMobileUser(Long mobileUserId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setMobileUser(mobileUserRepository.findById(mobileUserId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }




    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token has expired. Please make a new sign-in request.");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    @Transactional
    public int deleteByRiderId(Long riderId) {
        return refreshTokenRepository.deleteByRider(riderRepository.findById(riderId).get());
    }
}
