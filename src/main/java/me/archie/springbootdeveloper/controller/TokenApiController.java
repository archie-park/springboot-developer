package me.archie.springbootdeveloper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.archie.springbootdeveloper.dto.CreateAccessTokenResponse;
import me.archie.springbootdeveloper.service.RefreshTokenService;
import me.archie.springbootdeveloper.service.TokenService;

@RequiredArgsConstructor
@RestController
public class TokenApiController {
  private final TokenService tokenService;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/api/token")
  public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(@RequestBody CreateAccessTokenRequest request){
    String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

    return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
  }

  @DeleteMapping("/api/refresh-token")
  public ResponseEntity<Object> deleteRefreshToken() {
    refreshTokenService.delete();

    return ResponseEntity.ok().build();
  }
}
