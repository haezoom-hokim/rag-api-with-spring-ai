package io.rag.demo.ragapidemo.rag;

import io.rag.demo.ragapidemo.rag.dto.RagFilteredRequest;
import io.rag.demo.ragapidemo.rag.dto.RagQueryRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RagController {

  private final RagService ragService;

  // RAG 질의응답
  @PostMapping("/ask")
  public ResponseEntity<String> askQuestion(@RequestBody RagQueryRequest request) {
    String answer = ragService.ask(request.getQuery());
    return ResponseEntity.ok(answer);
  }

  // 상세한 RAG 결과
  @PostMapping("/ask/details")
  public ResponseEntity<RagDetails> askWithDetails(@RequestBody Map<String, String> request) {
    String query = request.get("query");
    RagDetails result = ragService.askWithDetails(query);
    return ResponseEntity.ok(result);
  }
}
