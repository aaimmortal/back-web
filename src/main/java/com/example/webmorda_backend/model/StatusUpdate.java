package com.example.webmorda_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatusUpdate {
    String agentid;
    String status;
    String paused;
    LocalDateTime lastCall;
}
