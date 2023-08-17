package com.example.webmorda_backend.payload;

import lombok.Data;

@Data
public class RegisterRequest {
    String login;
    String password;
    String repeatPassword;
}
