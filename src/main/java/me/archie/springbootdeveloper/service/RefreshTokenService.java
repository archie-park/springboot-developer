package me.archie.springbootdeveloper.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.config.jwt.TokenProvider;
import me.archie.springbootdeveloper.domain.RefreshToken;
import me.archie.springbootdeveloper.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;

  public RefreshToken findByRefreshToken(String refreshToken){
    return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
  }

  @Transactional
  public void delete() {
    String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
    Long userId = tokenProvider.getUserId(token);

    refreshTokenRepository.deleteByUserId(userId);
  }
}
