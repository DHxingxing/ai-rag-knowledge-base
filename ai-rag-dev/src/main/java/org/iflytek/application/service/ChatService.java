package org.iflytek.application.service;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.iflytek.domain.request.ChatInfoReq;
import org.iflytek.domain.request.GithubBaseChatReq;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

/**
 * @author hxdu5
 * @since 2025/6/29 14:55
 */
public interface ChatService {

    Flux<ChatResponse> chatWithRag(String ragTag, ChatInfoReq chatInfoReq);

    // 在 Spring MVC 中，一个接口方法只能有一个 @RequestBody 注解的参数。如果你写了多个 @RequestBody
    Flux<ChatResponse> chatWithGithubBaseRag(GithubBaseChatReq githubBaseChatRequest) throws GitAPIException;
}
