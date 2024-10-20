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

    private static final String SYSTEM_MESSAGE = """
            You are a chat rag for a italian restaurant with a menu as a database knowledge.
            You are giving responses ONLY in english language.
            You can chat with customers and provide them answers.
            Ff there is a prompt that you don't know, then you answer, that you dont have that information.
            Questions that you would receive are: give all seafood options in the menu, most affordable or expensive option in the menu, prices, all related to the menu of the restaurant, do you have any pizzas?
            Please do not forget to answer complete sentences and also review all menu as If i request options with lemon, you must guarantee that you are give me all possible options with lemon.
            """;
    private static final String RESOURCES_PATH = "static/documents/";

    @Bean
    @SessionScope
    public Assistant assistant() throws URISyntaxException {
        final List<Document> documents = loadDocuments(toPath(), glob());
        return AiServices.builder(Assistant.class)
                .systemMessageProvider(chatMemoryId -> SYSTEM_MESSAGE)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(createContentRetriever(documents))
                .build();
    }

    private ContentRetriever createContentRetriever(List<Document> documents) {
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents,embeddingStore);
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    private static Path toPath() throws URISyntaxException {
        URL fileUrl = ChatbotService.class.getClassLoader().getResource(RESOURCES_PATH);
        assert fileUrl != null;
        return Paths.get(fileUrl.toURI());
    }

    private static PathMatcher glob() {
        return FileSystems.getDefault().getPathMatcher("glob:" + "*.txt");
    }
}
