package io.rag.demo.ragapidemo.rag.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RagQueryRequest {

  @NotNull(message = "질문은 필수입니다")
  @Size(min = 1, max = 1000, message = "질문은 1자 이상 1000자 이하여야 합니다")
  private String query;

  // 선택적 파라미터들
  private Integer topK = 5; // 기본값 5
  private Double similarityThreshold; // 선택적 유사도 임계값
}