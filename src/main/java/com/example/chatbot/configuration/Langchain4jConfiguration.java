package com.example.chatbot.configuration;

import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;


@Configuration
@RequiredArgsConstructor
public class Langchain4jConfiguration {
    @Value("${ollama.model.name}")
    private String modelName;

    @Value("${ollama.host.url}")
    private String baseUrl;

    @Bean
    public ChatLanguageModel chatLanguageModel(){
        return OllamaChatModel.builder()
                .modelName(modelName)
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel(){
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }

    @Bean
    public EmbeddingStore embeddingStore(){
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(){
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300,0))
                .embeddingStore(embeddingStore())
                .embeddingModel(embeddingModel())
                .build();
    }


}
