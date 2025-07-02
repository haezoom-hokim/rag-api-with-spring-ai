package io.rag.demo.ragapidemo.rag.dto;

import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RagFilteredRequest {

  @NotNull(message = "질문은 필수입니다")
  @Size(min = 1, max = 1000, message = "질문은 1자 이상 1000자 이하여야 합니다")
  private String query;

  @Builder.Default
  private Map<String, String> filters = new HashMap<>();

  private Integer topK = 5; // 기본값 5
  private Double similarityThreshold; // 선택적 유사도 임계값

  // 편의 메서드들
  public void addFilter(String key, String value) {
    if (filters == null) {
      filters = new HashMap<>();
    }
    filters.put(key, value);
  }

  public boolean hasFilters() {
    return filters != null && !filters.isEmpty();
  }
}