package com.example.webmorda_backend.payload;

import lombok.Data;

@Data
public class AuthRequest {
    String login;
    String password;
}
