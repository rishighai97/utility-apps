package com.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Objects;

@RestController
@Slf4j
public class FileController {

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile, @RequestParam(value = "destinationDirectory", defaultValue = "/var/tmp/") String destinationDirectory) {
        log.info("Server received {} file", multipartFile.getOriginalFilename());
        saveFileToPath(multipartFile, destinationDirectory);
        return MessageFormat.format("{0} File saved to {1} destination successfully", multipartFile.getOriginalFilename(), destinationDirectory.concat(Objects.requireNonNull(multipartFile.getOriginalFilename())));
    }

    private void saveFileToPath(MultipartFile multipartFile, String destinationDirectory) {
        try {
            Path filepath = Paths.get(destinationDirectory, multipartFile.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(filepath)) {
                os.write(multipartFile.getBytes());
            }
        } catch (Exception e) {
            log.error("Exception occurred while saving multipart file on server", e);
            throw new RuntimeException(e);
        }
    }

}
