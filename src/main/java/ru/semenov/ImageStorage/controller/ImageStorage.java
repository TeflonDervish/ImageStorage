package ru.semenov.ImageStorage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/images")
public class ImageStorage {

    @Value("${file.storage}")
    private String folder;

    private final List<String> allowedMimeTypes = Arrays.asList(
            "image/jpeg",
            "image/png");

    @PostMapping("/upload")
    public ResponseEntity<String> update(@RequestParam("file") MultipartFile file) {
        try {
            String mimeType = file.getContentType();
            if (mimeType == null || !allowedMimeTypes.contains(mimeType.toLowerCase())) {
                return ResponseEntity.badRequest().body("Разрешены только изображения (JPEG, PNG, GIF, WEBP).");
            }
            String fileName = file.hashCode() + file.getOriginalFilename();
            File dest = new File(folder + "\\" + fileName);
            file.transferTo(dest);
            return ResponseEntity.ok(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки файла. " + e);
        }
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String fileName) {
        try {
            File file = new File(folder + "\\" + fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(Files.probeContentType(Paths.get(file.getAbsoluteFile().toURI()))));
            headers.setContentDispositionFormData("inline", fileName);
            return ResponseEntity.ok()
                    .header(headers)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}


