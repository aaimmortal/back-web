package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.User;
import com.example.webmorda_backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class UserController {
    UserService userService;
    public ResponseEntity<?> getUser(@RequestParam(name = "login") String login){
        User user = userService.getUser(login);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    public ResponseEntity<Resource> getImage(@RequestParam(name = "file") String file) throws IOException {
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
}
