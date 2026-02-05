package com.epam.rd.autocode.spring.project.util;

import com.epam.rd.autocode.spring.project.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

// ADD VALIDATION SIZE OF FILE/ розширення файлу
public class FileUploadUtil {

    public static String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            String projectDir = System.getProperty("user.dir");
            Path uploadPath = Paths.get(projectDir, "uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename(), e);
        }
    }
}