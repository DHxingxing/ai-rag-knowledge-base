package org.iflytek.infrastructure.file;

import jakarta.annotation.Resource;
import org.iflytek.domain.service.FileProgressService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hxdu5
 * @since 2025/6/28 22:44
 */
@Service
public class FileProgressServiceImpl implements FileProgressService {

    @Resource
    TokenTextSplitter tokenTextSplitter;


    @Override
    public List<Document> setMetaData(String path, String ragTag) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new FileSystemResource(path));
        List<Document> documents = tikaDocumentReader.get();
        List<Document> documentList = tokenTextSplitter.apply(documents);
        documentList.forEach(document -> document.getMetadata().put("knowledge",ragTag));
        return documentList;
    }
}
