package com.example.webmorda_backend.model;

import lombok.Data;

@Data
public class AgentRequest {
    String agentName;
    String agentSecret;
    String agentContext;
}
