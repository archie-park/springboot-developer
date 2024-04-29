package me.archie.springbootdeveloper.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.archie.springbootdeveloper.domain.User;
import me.archie.springbootdeveloper.dto.AddUserRequest;
import me.archie.springbootdeveloper.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  public Long save(AddUserRequest dto) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return userRepository
        .save(User.builder()
          .email(dto.getEmail())
          // 패스워드 암호화
          .password(encoder.encode(dto.getPassword()))
          .build()
        ).getId();
  }

  // 메서드 추가
  public User findById(Long userId){
    return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }

  public User findByEmail(String email){
    return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
  }
}
