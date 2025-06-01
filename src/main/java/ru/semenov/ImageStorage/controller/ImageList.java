package ru.semenov.ImageStorage.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ImageList {

    private static final Log log = LogFactory.getLog(ImageList.class);

    @Value("${file.storage}")
    private String folder;

    @Value("${url.address}")
    private String url;

    @Value("${server.port}")
    private String port;

    @GetMapping("/preview")
    public String preview(Model model) {
        try {
            log.info(folder);
            Path uploadPath = Paths.get(folder);
            List<String> imageUrls = new ArrayList<>();

            // Получаем список всех файлов в директории
            Files.list(uploadPath).forEach(file -> {
                String fileName = file.getFileName().toString();
                String fileUrl = "http://" + url + "/api/images/" + fileName;
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
