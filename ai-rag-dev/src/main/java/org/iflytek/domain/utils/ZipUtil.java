package org.iflytek.domain.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author hxdu5
 * @since 2025/6/26 16:02
 */
@Slf4j
public class ZipUtil {


    /**
     * 解压ZIP文件并提取所有MD文件
     *
     * @param zipFile ZIP文件
     * @param extractPath 解压目标路径
     * @return 提取到的MD文件路径列表
     * @throws IOException 解压过程中可能抛出的IO异常
     */
    public static List<String> unZipFiles(MultipartFile zipFile, String extractPath) throws IOException {
        List<String> mdFilePaths = new ArrayList<>();
        // 创建解压目录
        Path extractDir = Paths.get(extractPath);
        if (!Files.exists(extractDir)) {
            Files.createDirectories(extractDir);
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String fileName = entry.getName();
                // 检查是否为MD文件
                if (fileName.toLowerCase().endsWith(".md")) {
                    // 创建目标文件路径
                    Path targetPath = extractDir.resolve(fileName);
                    // 确保父目录存在
                    Path parentDir = targetPath.getParent();
                    if (!Files.exists(parentDir)) {
                        Files.createDirectories(parentDir);
                    }
                    // 写入文件
                    try (FileOutputStream fos = new FileOutputStream(targetPath.toFile())) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                    mdFilePaths.add(targetPath.toString());
                    log.info("提取MD文件: {}", targetPath);
                }
                zipInputStream.closeEntry();
            }
        }
        log.info("解压完成，共提取到 {} 个MD文件", mdFilePaths.size());
        return mdFilePaths;
    }

    /**
     * 检查文件是否为ZIP格式
     *
     * @param file 要检查的文件
     * @return 如果是ZIP文件返回true，否则返回false
     */
    public static boolean isZipFile(MultipartFile file) {
        try {
            byte[] magic = new byte[4];
            try (InputStream is = file.getInputStream()) {
                if (is.read(magic) != 4) {
                    return false;
                }
            }
            // ZIP文件的魔数是 0x504B0304
            return magic[0] == 0x50 && magic[1] == 0x4B && magic[2] == 0x03 && magic[3] == 0x04;
        } catch (IOException e) {
            log.error("检查ZIP文件格式时出错", e);
            return false;
        }
    }

}
