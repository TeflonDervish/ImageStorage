package ru.semenov.ImageStorage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageStorage {

    @Value("${file.storage}")
    private String folder;

    private final List<String> allowedMimeTypes = Arrays.asList(
            "image/jpeg",
            "image/png");

    @PostMapping("/upload")
    public ResponseEntity<String> update(@RequestParam("file") MultipartFile file) {
        try {
            // Создаем директорию, если она не существует
            Path uploadPath = Paths.get(folder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Генерируем уникальное имя файла
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // Сохраняем файл
            Files.copy(file.getInputStream(), filePath);

            // Возвращаем URL для доступа к файлу
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
           Path filePath = Paths.get(folder).resolve(fileName);
            byte[] imageBytes = Files.readAllBytes(filePath);
            return ResponseEntity.ok().body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}


