package org.iflytek.infrastructure.config;


import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.OllamaEmbeddingClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class OllamaConfig {

    /**
     * 创建OllamaApi Bean
     * 从配置文件中读取Ollama服务的基础URL，创建OllamaApi实例
     * 配置路径：spring.ai.ollama.base-url
     *
     * @param baseUrl Ollama服务的基础URL，从application-dev.yml中注入
     * @return OllamaApi实例，用于与Ollama服务通信
     */
    @Bean
    public OllamaApi ollamaApi(@Value("${spring.ai.ollama.base-url}") String baseUrl){
        return new OllamaApi(baseUrl);
    }

    /**
     * 创建OllamaChatClient Bean
     * 基于OllamaApi创建聊天客户端，用于与Ollama模型进行对话交互
     * 支持流式响应和多种模型调用
     *
     * @param ollamaApi OllamaApi实例，由Spring自动注入f
     * @return OllamaChatClient实例，用于聊天对话
     */
    @Bean
    public OllamaChatClient ollamaChatClient(OllamaApi ollamaApi){
        return new OllamaChatClient(ollamaApi);
    }

    /**
     * 创建TokenTextSplitter Bean
     * 用于文本分割的组件，在RAG系统中将长文本分割成适合向量化的片段
     * 支持按token数量分割，确保每个片段都在模型的最大token限制内
     *
     * @return TokenTextSplitter实例，用于文本分割
     */
    @Bean
    public TokenTextSplitter tokenTextSplitter(){
        return new TokenTextSplitter();
    }

    /**
     * 创建SimpleVectorStore Bean
     * 用于向量存储的组件，支持文档的向量化和相似性搜索
     * 配置嵌入模型用于文本向量化
     *
     * @param ollamaApi OllamaApi实例，由Spring自动注入
     * @param embeddingModel 嵌入模型名称，从配置文件中注入
     * @return SimpleVectorStore实例，用于向量存储
     */
    @Bean
    public SimpleVectorStore simpleVectorStore(OllamaApi ollamaApi,
                                               @Value("${spring.ai.ollama.embedding.model}") String embeddingModel){
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel(embeddingModel));
        return new SimpleVectorStore(embeddingClient);
    }

    @Bean
    public PgVectorStore pgVectorStore(OllamaApi ollamaApi, JdbcTemplate jdbcTemplate){
        OllamaEmbeddingClient embeddingClient = new OllamaEmbeddingClient(ollamaApi);
        embeddingClient.withDefaultOptions(OllamaOptions.create().withModel("nomic-embed-text"));
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }

}
