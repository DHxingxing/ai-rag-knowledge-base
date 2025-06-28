package org.iflytek.infrastructure.git;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.iflytek.domain.request.UserBaseReq;
import org.iflytek.domain.service.GitService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author hxdu5
 * @since 2025/6/28 22:09
 */
@Service
@Slf4j
public class GitServiceImpl implements GitService{

    @Override
    public Git cloneBase(UserBaseReq userBaseReq) throws GitAPIException {
        return Git.cloneRepository()
                .setURI(userBaseReq.getUrl())
                .setDirectory(new File(userBaseReq.getLocalPath()))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userBaseReq.getUserName(),userBaseReq.getToken()))
                .call();
    }

    @Override
    public String analyzeGithubBase(UserBaseReq userBaseReq) {
        try (Git git = cloneBase(userBaseReq)) {


            // 你的业务逻辑
        } catch (Exception e) {
            log.error("克隆Git仓库失败: {}", e.getMessage(), e);
            throw new RuntimeException("<UNK>" + e.getMessage());
        }
        return "";

    }
}
