package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.User;
import com.example.webmorda_backend.payload.UploadImageRequest;
import com.example.webmorda_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class UserController {
    UserService userService;

    public ResponseEntity<?> getUser(@RequestParam(name = "login") String login) {
        User user = userService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public ResponseEntity<Resource> getImage(@RequestParam(name = "login") String login) throws IOException {
        User user = userService.getUser(login);
        String file = user.getAvatar();
        Path imagePath = Paths.get("/home/azamat/Documents/avatars/" + file);
        Resource resource = new UrlResource(imagePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
            return ResponseEntity.ok().headers(headers).body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody UploadImageRequest uploadImageRequest) {
        try {
            String login = uploadImageRequest.getLogin();
            byte[] file = uploadImageRequest.getFile();
            String directory = "/home/azamat/Documents/avatars/";
            User user = userService.getUser(login);
            String fileName = uploadImageRequest.getLogin() + "_" + UUID.randomUUID();
            user.setAvatar(fileName);
            Files.write(Paths.get(directory + fileName), file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error uploading image:");
        }
    }
}
