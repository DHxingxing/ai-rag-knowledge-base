package org.iflytek.domain.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.iflytek.domain.request.UserBaseReq;

import java.nio.file.FileVisitResult;

/**
 * @author hxdu5
 * @since 2025/6/28 22:09
 */
public interface GitService {

    Git cloneBase(UserBaseReq userBaseReq) throws GitAPIException;

    String analyzeGithubBase(UserBaseReq userBaseReq) throws GitAPIException;

    FileVisitResult visitLocalBase(String path, String projectName) throws GitAPIException;
}
