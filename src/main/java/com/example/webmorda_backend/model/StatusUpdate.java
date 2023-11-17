package com.example.webmorda_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusUpdate {
    String agentid;
    String status;
    String paused;
}
