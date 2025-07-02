package io.rag.demo.ragapidemo.rag;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder.Op;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

  private final VectorStore vectorStore;
  private final ChatModel chatModel; // OllamaChatModel 또는 다른 ChatModel 구현체

  public String ask(String query) {
    return ask(query, 5);
  }

  public String ask(String query, int topK) {
    // 1. 벡터 검색으로 관련 문서 검색
    List<Document> similarDocuments = vectorStore.similaritySearch(
        SearchRequest.builder().query(query).topK(topK).build()
    );

    // 2. 검색된 문서들을 컨텍스트로 구성
    String context = buildContext(similarDocuments);

    // 3. 시스템 프롬프트 구성
    String systemPrompt = buildSystemPrompt(context);

    // 4. ChatModel을 사용해 응답 생성
    ChatResponse response = chatModel.call(
        new Prompt(List.of(
            new SystemMessage(systemPrompt),
            new UserMessage(query)
        ))
    );

    return response.getResult().getOutput().getText();
  }

  // 유사도 임계값을 적용한 검색
  public String askWithThreshold(String query, double similarityThreshold) {
    List<Document> similarDocuments = vectorStore.similaritySearch(
        SearchRequest.builder().query(query)
            .topK(10)
            .similarityThreshold(similarityThreshold)
            .build()
    );

    if (similarDocuments.isEmpty()) {
      return "죄송합니다. 관련된 정보를 찾을 수 없습니다.";
    }

    String context = buildContext(similarDocuments);
    String systemPrompt = buildSystemPrompt(context);

    ChatResponse response = chatModel.call(
        new Prompt(List.of(
            new SystemMessage(systemPrompt),
            new UserMessage(query)
        ))
    );

    return response.getResult().getOutput().getText();
  }

  // 상세한 RAG 결과 반환
  public RagDetails askWithDetails(String query) {
    List<Document> similarDocuments = vectorStore.similaritySearch(
        SearchRequest.builder().query(query).topK(5).build()
    );

    String context = buildContext(similarDocuments);
    String systemPrompt = buildSystemPrompt(context);

    ChatResponse response = chatModel.call(
        new Prompt(List.of(
            new SystemMessage(systemPrompt),
            new UserMessage(query)
        ))
    );

    return RagDetails.builder()
        .answer(response.getResult().getOutput().getText())
        .sourceDocuments(similarDocuments)
        .query(query)
        .contextUsed(context)
        .documentCount(similarDocuments.size())
        .build();
  }

  private String buildContext(List<Document> documents) {
    if (documents != null && documents.isEmpty()) {
      return "관련 정보가 없습니다.";
    }

    StringBuilder contextBuilder = new StringBuilder();
    contextBuilder.append("다음은 관련된 정보입니다:\n\n");

    for (int i = 0; i < Objects.requireNonNull(documents).size(); i++) {
      Document doc = documents.get(i);
      contextBuilder.append(String.format("[문서 %d]\n", i + 1));
      contextBuilder.append(doc.getText()).append("\n");

      // 메타데이터가 있으면 추가
      if (!doc.getMetadata().isEmpty()) {
        contextBuilder.append("메타데이터: ").append(doc.getMetadata()).append("\n");
      }
      contextBuilder.append("\n");
    }

    return contextBuilder.toString();
  }

  private String buildSystemPrompt(String context) {
    return String.format("""
        당신은 사용자 정보 검색 도우미입니다.
        제공된 컨텍스트를 바탕으로 사용자의 질문에 정확하고 도움이 되는 답변을 제공해주세요.
        
        컨텍스트에 없는 정보는 추측하지 마시고, 모르겠다고 말씀해주세요.
        가능한 한 구체적이고 상세한 답변을 제공해주세요.
        
        컨텍스트:
        %s
        
        위 정보를 바탕으로 사용자의 질문에 답변해주세요.
        """, context);
  }
}