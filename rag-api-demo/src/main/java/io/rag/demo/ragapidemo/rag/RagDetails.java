package io.rag.demo.ragapidemo.rag;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.ai.document.Document;

@Data
@Builder
public class RagDetails {
  private String answer;
  private List<Document> sourceDocuments;
  private String query;
  private String contextUsed;
  private int documentCount;
}
