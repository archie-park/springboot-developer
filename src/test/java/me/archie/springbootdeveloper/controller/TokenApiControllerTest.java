package me.archie.springbootdeveloper.controller;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.archie.springbootdeveloper.config.jwt.JwtFactory;
import me.archie.springbootdeveloper.config.jwt.JwtProperties;
import me.archie.springbootdeveloper.domain.RefreshToken;
import me.archie.springbootdeveloper.domain.User;
import me.archie.springbootdeveloper.dto.CreateAccessTokenRequest;
import me.archie.springbootdeveloper.repository.RefreshTokenRepository;
import me.archie.springbootdeveloper.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TokenApiControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  JwtProperties jwtProperties;
  @Autowired
  UserRepository userRepository;
  @Autowired
  RefreshTokenRepository refreshTokenRepository;

  User user;

  @BeforeEach
  public void mockMvcSetup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    userRepository.deleteAll();
  }

  @BeforeEach
  void setSecurityContext() {
    userRepository.deleteAll();
    user = userRepository.save(User.builder().email("user@email.com").password("test").build());

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
  }

  @DisplayName("createNewAccessToken(): 새로운 엑세스 토큰을 발급한다.")
  @Test
  void testCreateNewAccessToken() throws Exception {
    // given
    final String url = "/api/token";

    String refreshToken = createRefreshToken();
    refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

    CreateAccessTokenRequest request = new CreateAccessTokenRequest();
    request.setRefreshToken(refreshToken);
    final String requestBody = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));
    //then
    resultActions.andExpect(status().isCreated()).andExpect(jsonPath("$.accessToken").isNotEmpty());
  }

  @DisplayName("deleteRefreshToken: 리프레시 토큰을 삭제한다.")
  @Test
  public void deleteRefreshToken() throws Exception{
    // given
    final String url = "/api/refresh-token";
    String refreshToken = createRefreshToken();

    refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));

    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(user, refreshToken, user.getAuthorities()));

    // when
    ResultActions resultActions = mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON_VALUE));

    // then
    resultActions.andExpect(status().isOk());
    assertThat(refreshTokenRepository.findByRefreshToken(refreshToken)).isEmpty();
  }

  private String createRefreshToken() {
    return JwtFactory.builder().claims(Map.of("id", user.getId())).build().createToken(jwtProperties);
  }
}
