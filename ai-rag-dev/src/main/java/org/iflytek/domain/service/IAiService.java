package org.iflytek.domain.service;

import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

/**
 * AI 服务接口
 * 提供与 AI 模型交互的基础能力，包括同步和异步的对话生成功能
 * 支持多种 AI 模型的调用，如 Ollama、OpenAI 等
 */
public interface IAiService {

    /**
     * 同步生成对话响应
     * 
     * @param model 模型名称，用于指定使用的 AI 模型（如 "llama2"、"gpt-3.5-turbo" 等）
     * @param message 用户输入的对话消息
     * @return ChatResponse 包含 AI 模型的响应内容
     * @throws RuntimeException 当模型调用失败或参数无效时抛出
     */
    ChatResponse generate(@RequestParam String model, @RequestParam String message);

    /**
     * 流式生成对话响应
     * 使用响应式编程方式，支持流式返回 AI 模型的生成结果
     * 适用于需要实时展示生成过程的场景
     * 
     * @param model 模型名称，用于指定使用的 AI 模型（如 "llama2"、"gpt-3.5-turbo" 等）
     * @param message 用户输入的对话消息
     * @return Flux<ChatResponse> 返回一个响应式流，包含 AI 模型的流式响应内容
     * @throws RuntimeException 当模型调用失败或参数无效时抛出
     */
    Flux<ChatResponse> generateStream(@RequestParam String model, @RequestParam String message);
}
