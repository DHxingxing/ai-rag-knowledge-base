package org.iflytek.infrastructure.file;

import jakarta.annotation.Resource;
import org.apache.tika.Tika;
import org.iflytek.domain.service.FileProgressService;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hxdu5
 * @since 2025/6/28 22:44
 */
@Service
public class FileProgressServiceImpl implements FileProgressService {

    @Resource
    TokenTextSplitter tokenTextSplitter;


    @Override
    public List<Document> setMetaData(String path, String ragTag) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(new File(path));
        if (!mimeType.startsWith("text") && !mimeType.equals("application/json") && !mimeType.equals("application/xml")) {
            return Collections.emptyList();
        }
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new FileSystemResource(path));
        List<Document> documents = tikaDocumentReader.get();
        if(CollectionUtils.isEmpty(documents)) {
            return Collections.emptyList();
        }
        List<Document> validDocuments = documents.stream()
                .filter(doc -> doc.getContent() != null || !doc.getContent().trim().isEmpty())
                .toList();
        List<Document> documentList = tokenTextSplitter.apply(validDocuments);
        documentList.forEach(document -> document.getMetadata().put("knowledge",ragTag));
        return documentList;
    }
}
