package org.iflytek.domain.service;

import org.springframework.ai.document.Document;

import java.io.IOException;
import java.util.List;

/**
 * @author hxdu5
 * @since 2025/6/28 22:43
 */
public interface FileProgressService {

    List<Document> setMetaData(String path, String ragTag) throws IOException;
}
