package com.javaPro.myProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传服务
 * 使用本地存储进行文件上传
 */
@Service
public class FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    // 本地上传目录
    private final String localUploadDir = System.getProperty("user.dir") + "/uploads/";

    /**
     * 上传文件到本地存储
     *
     * @param file 上传的文件
     * @return 文件访问路径
     * @throws IOException 文件上传异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("文件名为空");
        }

        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }

        String fileName = UUID.randomUUID().toString() + extension;

        logger.info("上传文件: {} -> {}", originalFilename, fileName);
        return uploadToLocal(file, fileName);
    }

    private String uploadToLocal(MultipartFile file, String fileName) throws IOException {
        // 确保上传目录存在
        Path uploadPath = Paths.get(localUploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 保存文件到本地
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        logger.info("文件已保存到: {}", filePath.toString());

        // 返回本地访问URL
        return "/uploads/" + fileName;
    }
}
