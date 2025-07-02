package io.rag.demo.ragapidemo.user;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEmbeddingService {

  private final UserRepository userRepository;
  private final VectorStore vectorStore;

  public void embedAllUsers() {
    List<User> users = userRepository.findAll();

    List<Document> docs = users.stream()
        .map(user -> new Document(
            // 사용자 정보를 구조화된 텍스트로 변환
            buildUserContent(user),
            // 메타데이터 추가
            Map.of(
                "userId", user.getId().toString(),
                "name", user.getName(),
                "age", user.getAge().toString(),
                "email", user.getEmail(),
                "type", "user_profile"
            )
        )).toList();

    // Spring AI 1.0.0에서는 vectorStore.add()가 자동으로 임베딩을 처리
    vectorStore.add(docs);
  }

  private String buildUserContent(User user) {
    StringBuilder content = new StringBuilder();
    content.append("사용자 이름: ").append(user.getName()).append("\n");
    content.append("나이: ").append(user.getAge()).append("세\n");
    content.append("이메일: ").append(user.getEmail()).append("\n");
    content.append("주소: ").append(user.getAddress()).append("\n");

    // 검색 가능한 추가 정보
    content.append("프로필: ").append(user.getName())
        .append("님은 ").append(user.getAge())
        .append("세이며, ").append(user.getAddress())
        .append("에 거주하고 있습니다.");

    return content.toString();
  }

  // 특정 사용자만 임베딩
  public void embedUser(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

    Document doc = new Document(
        buildUserContent(user),
        Map.of(
            "userId", user.getId().toString(),
            "name", user.getName(),
            "age", user.getAge().toString(),
            "email", user.getEmail(),
            "type", "user_profile"
        )
    );

    vectorStore.add(List.of(doc));
  }

  // 사용자 정보 업데이트 시 벡터 스토어도 업데이트
  public void updateUserEmbedding(Long userId) {
    // 기존 문서 삭제 (userId로 필터링)
    vectorStore.delete(List.of(userId.toString()));

    // 새로운 문서 추가
    embedUser(userId);
  }

  // 사용자 삭제 시 벡터 스토어에서도 제거
  public void deleteUserEmbedding(Long userId) {
    vectorStore.delete(List.of(userId.toString()));
  }
}