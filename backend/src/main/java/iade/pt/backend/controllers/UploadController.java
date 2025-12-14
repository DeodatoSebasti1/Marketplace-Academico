package iade.pt.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final String UPLOAD_DIR = "/Users/deodatoluzayadio/academicplace/backend/uploads/";

    @PostMapping
    public ResponseEntity<?> uploadImagem(@RequestParam("file") MultipartFile file) {
        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) directory.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = UPLOAD_DIR + fileName;
            file.transferTo(new File(filePath));

            String imageUrl = "http://localhost:8080/uploads/" + fileName;

            return ResponseEntity.ok(new UploadResponse(imageUrl));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    static class UploadResponse {
        public String imageUrl;

        public UploadResponse(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
