package com.epam.rd.autocode.spring.project.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Project Root Directory dynamically
        String projectDir = System.getProperty("user.dir");

        // Absolute path to "uploads"
        Path uploadPath = Paths.get(projectDir, "uploads");

        registry.addResourceHandler("/content/**")
                .addResourceLocations("file:" + uploadPath.toAbsolutePath() + "/");
    }
}