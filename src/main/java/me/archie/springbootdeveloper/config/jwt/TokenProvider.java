package me.archie.springbootdeveloper.config.jwt;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.domain.User;

@RequiredArgsConstructor
@Service
public class TokenProvider {

  private final JwtProperties jwtProperties;

  public String generateToken(User user, Duration expiredAt) {
    Date now = new Date();
    return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
  }

  // JWT 토큰 생성 메서드
  private String makeToken(Date expiry, User user) {
    Date now = new Date();
    System.out.println("[makeToken]");
    String token = Jwts.builder()
        // .header().add("typ", "JWT").and() // 헤더 typ: JWT
        .issuer(jwtProperties.getIssuer()) // 내용 iss: archie.yiwoo@gmail.com(properties 파일에서 설정한 값)
        .issuedAt(now) // 내용 iat : 현재 시간
        .expiration(expiry) // 내용 exp: expiry 멤버 변숫값
        .subject(user.getEmail()) // 내용 sub : 유저의 이메일
        .claim("id", user.getId()) // 클레임 id : 유저 ID
        .signWith(jwtProperties.getSecretKey()) // 서명 : secretkey로 암호와
        .compact();
    
    System.out.println("[token] = " + token);
    return token;
  }

  // JWT 토큰 유효성 검증 메서드
  public boolean validToken(String token) {
    try {
      Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build() // secretkey로 복호화
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) { // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
      return false;
    }
  }

  // 토큰 기반으로 인증 정보를 가져오는 메서드
  public Authentication getAuthentication(String token) {
    Claims claims = getClaims(token);
    Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

    return new UsernamePasswordAuthenticationToken(
        new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token,
        authorities);
  }

  // 토큰 기반으로 유저 ID를 가져오는 메서드
  public Long getUserId(String token){
    Claims claims = getClaims(token);
    return claims.get("id", Long.class);
  }

  private Claims getClaims(String token) {
    return Jwts.parser() // 클레임 조회
                .verifyWith(jwtProperties.getSecretKey()).build()
                .parseSignedClaims(token)
                .getPayload();
  }
}
