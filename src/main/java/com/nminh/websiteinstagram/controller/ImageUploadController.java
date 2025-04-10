package com.nminh.websiteinstagram.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/images")
public class ImageUploadController {
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // Loại bỏ các ký tự đặc biệt
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName); // tạo đường dẫn đầy đủ đến file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING); // lưu file , nếu có thì ghi đè

        String imageUrl = "http://localhost:8080/images/" + fileName;
        return ResponseEntity.ok(imageUrl); // URL lưu vào DB
    }
}

