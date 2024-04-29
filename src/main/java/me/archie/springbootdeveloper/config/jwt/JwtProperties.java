package me.archie.springbootdeveloper.config.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt") // 자바 클래스에 프로퍼티값을 가져와서 사용하는 애너테이션
public class JwtProperties {
  private String issuer;
  private String publicKey;
  private SecretKey secretKey;

  @PostConstruct
  protected void init() {
    publicKey = Base64.getEncoder().encodeToString(publicKey.getBytes(StandardCharsets.UTF_8));
    secretKey = Keys.hmacShaKeyFor(publicKey.getBytes(StandardCharsets.UTF_8));
  }
}
