package me.archie.springbootdeveloper.config.jwt;

import static java.util.Collections.*;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtFactory {
  private String subject = "test@email.com";
  private Date issuedAt = new Date();
  private Date expiration = new Date(new Date().getTime() + Duration.ofDays(14).toMillis());
  private Map<String, Object> claims = emptyMap();

  // 빌더 패턴을 사용해 설정이 필요한 데이터만 선택 설정
  @Builder
  public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
    this.subject = subject != null ? subject : this.subject;
    this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
    this.expiration = expiration != null ? expiration : this.expiration;
    this.claims = claims != null ? claims : this.claims;
  }

  public static JwtFactory withDefaultValues() {
    return JwtFactory.builder().build();
  }

  // jjwt 라이브러리를 사용해 JWT 토큰 생성
  public String createToken(JwtProperties jwtProperties) {
    return Jwts.builder()
                .subject(subject)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .claims(claims)
                .signWith(jwtProperties.getSecretKey())
                .compact();
  }
}
