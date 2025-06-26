/**
 * Application Layer - Service Package (应用层 - 服务包)
 * 
 * 本包包含应用层的服务接口和实现，负责业务流程的编排：
 * - RagService: RAG系统应用服务接口
 * - IAiService: AI服务接口
 * - impl/: 服务实现类
 * 
 * 应用层不包含业务规则，而是通过协调领域对象来实现用例。
 *
 */
package org.iflytek.domain.service;


import org.iflytek.domain.common.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * RAG应用服务接口
 * 
 * 定义RAG系统的应用层业务操作，协调领域服务和基础设施。
 * 应用服务是薄服务，只负责业务流程的编排，不包含具体的业务规则。
 * 
 * @author hxdu5
 */


public interface RagService {

    Response<List<String>> ragModelList();

    Response<String> uploadFile(String ragTag, List<MultipartFile> files);

    Response<String> chatWithRag(String filePath, String model, String message);

}
