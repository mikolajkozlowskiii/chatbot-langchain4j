# Simple RAG Chatbot using Spring Boot, LangChain4j and Ollama

This project is a simple chatbot implementation using Spring Boot, Ollama (a platform for running LLMs on local machine) and LangChain (a framework for using LLMs in app). The chatbot can respond to user queries using a predefined knowledge base and natural language processing.

It's just example project, which uses LLM llama3.1:8b and knowledge base consisting italian restaurant menu. You can call agent with prompts about dishes from menu. You can change these properties manually and create assistant for your own purposes.
## Requirements
For building and running the application you need:
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 3.6](https://maven.apache.org)
- [Ollama](https://ollama.com/download)


## Running the Project
1. Run model on Ollama platform. In our case it's llama3.1:8b model:
```shell
ollama run llama3.1:8b
```

2. There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.example.chatbot.ChatbotApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:
```shell
mvn spring-boot:run
```

3. Now you can call endpoint with prompt in body, you can do this in for example in postman or from terminal using curl:
```shell
curl -X POST http://localhost:8080/api/v1/chat/ \
-H "Content-Type: application/json" \
-d '{"message": "Hi, do you have dishes with lemon?"}' 
```

