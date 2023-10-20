package com.example.webmorda_backend.payload;

import lombok.Data;


@Data
public class UploadImageRequest {
    String login;
    byte[] file;
}
