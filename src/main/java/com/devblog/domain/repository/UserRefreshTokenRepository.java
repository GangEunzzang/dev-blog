package com.devblog.domain.repository;

import com.devblog.domain.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByEmail(String userEmail);
    UserRefreshToken findByEmailAndRefreshToken(String userEmail, String refreshToken);
}
