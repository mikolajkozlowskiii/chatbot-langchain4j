package com.example.chatbot.configuration;

import com.example.chatbot.service.Assistant;
import com.example.chatbot.service.ChatbotService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocuments;

@Configuration
@RequiredArgsConstructor
public class AssistantConfiguration {
    private final ChatLanguageModel chatLanguageModel;

    @Bean
    @SessionScope
    public Assistant assistant() throws URISyntaxException {
        List<Document> document = loadDocuments(toPath("static/documents/"), glob("*.txt"));
        return AiServices.builder(Assistant.class)
                // .systemMessageProvider(chatMemoryId -> systemMessage)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(document))
                .build();
    }

    private ContentRetriever createContentRetriever(List<Document> documents) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents,embeddingStore);
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    private static Path toPath(String relativePath) throws URISyntaxException {
        URL fileUrl = ChatbotService.class.getClassLoader().getResource(relativePath);
        assert fileUrl != null;
        return Paths.get(fileUrl.toURI());
    }

    private static PathMatcher glob(String glob) {
        return FileSystems.getDefault().getPathMatcher("glob:" + glob);
    }
}
