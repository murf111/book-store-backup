package com.epam.rd.autocode.spring.project.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null; // No image uploaded
        }

        // Create directory if it doesn't exist
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename (uuid_originalName.jpg) to prevent duplicates
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Save the file
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }
}