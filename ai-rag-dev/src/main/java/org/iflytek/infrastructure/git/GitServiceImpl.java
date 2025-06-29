package org.iflytek.infrastructure.git;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.iflytek.domain.request.UserBaseReq;
import org.iflytek.domain.service.FileProgressService;
import org.iflytek.domain.service.GitService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author hxdu5
 * @since 2025/6/28 22:09
 */
@Service
@Slf4j
public class GitServiceImpl implements GitService{

    @Resource
    FileProgressService fileProgressService;
    @Resource
    PgVectorStore pgVectorStore;

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

        String projectName = extractProjectName(userBaseReq.getUrl());
        int[] fileCount = {0};

        try (Git git = cloneBase(userBaseReq)) {
            Files.walkFileTree(Paths.get(userBaseReq.getLocalPath()), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    List<Document> documents = fileProgressService.setMetaData(file.toString(), projectName);
                    pgVectorStore.accept(documents);
                    fileCount[0]++;
                    return FileVisitResult.CONTINUE;
                }
            });
            return String.format("项目[%s]分析完成，共处理文件数：%d", projectName, fileCount[0]);
        } catch (Exception e) {
            log.error("克隆Git仓库失败: {}", e.getMessage(), e);
            throw new RuntimeException("<UNK>" + e.getMessage());
        }
    }

    private String extractProjectName(String repoUrl) {
        String[] parts = repoUrl.split("/");
        String projectNameWithGit = parts[parts.length - 1];
        return projectNameWithGit.replace(".git", "");
    }

}
