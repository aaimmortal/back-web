package com.example.webmorda_backend.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadImageRequest {
    String login;
    MultipartFile file;
}
