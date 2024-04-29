package me.archie.springbootdeveloper.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.archie.springbootdeveloper.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUserId(Long userId);
  Optional<RefreshToken> findByRefreshToken(String refreshToken);
  void deleteByUserId(Long userId);
}
