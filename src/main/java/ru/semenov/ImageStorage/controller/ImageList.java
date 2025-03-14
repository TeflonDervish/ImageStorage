package ru.semenov.ImageStorage.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageList {

    @Value("${file.storage}")
    private String folder;

    @GetMapping("/preview")
    public String preview(Model model) {
        try {
            Path uploadPath = Paths.get(folder);
            List<String> imageUrls = new ArrayList<>();

            // Получаем список всех файлов в директории
            Files.list(uploadPath).forEach(file -> {
                String fileName = file.getFileName().toString();
                String fileUrl = "http://localhost:8090/api/images/" + fileName;
                imageUrls.add(fileUrl);
            });

            // Передаем список изображений в шаблон
            model.addAttribute("images", imageUrls);
            return "images.html";
        } catch (IOException e) {
            return "images.html";
        }
    }

}
