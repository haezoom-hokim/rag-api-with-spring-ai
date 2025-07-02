package io.rag.demo.ragapidemo.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserRepository userRepository;
  private final UserEmbeddingService userEmbeddingService;

  // 모든 사용자 임베딩
  @PostMapping("/embed")
  public ResponseEntity<String> embedAllUsers() {
    userEmbeddingService.embedAllUsers();
    return ResponseEntity.ok("모든 사용자 임베딩 완료");
  }

  // 특정 사용자 임베딩
  @PostMapping("/{userId}/embed")
  public ResponseEntity<String> embedUser(@PathVariable Long userId) {
    userEmbeddingService.embedUser(userId);
    return ResponseEntity.ok("사용자 임베딩 완료");
  }

  @GetMapping
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok(userRepository.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userRepository.save(user);
    // 새 사용자 자동 임베딩
    userEmbeddingService.embedUser(savedUser.getId());
    return ResponseEntity.ok(savedUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
    return userRepository.findById(id)
        .map(existingUser -> {
          existingUser.setName(user.getName());
          existingUser.setAge(user.getAge());
          existingUser.setAddress(user.getAddress());
          existingUser.setEmail(user.getEmail());
          User updatedUser = userRepository.save(existingUser);
          // 업데이트된 사용자 재임베딩
          userEmbeddingService.updateUserEmbedding(id);
          return ResponseEntity.ok(updatedUser);
        })
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    return userRepository.findById(id)
        .map(user -> {
          userRepository.delete(user);
          // 벡터 스토어에서도 삭제
          userEmbeddingService.deleteUserEmbedding(id);
          return ResponseEntity.ok().<Void>build();
        })
        .orElse(ResponseEntity.notFound().build());
  }
} 