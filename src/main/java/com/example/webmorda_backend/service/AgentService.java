package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.Agent;
import com.example.webmorda_backend.repository.AgentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgentService {
    AgentRepository agentRepository;

    public List<Agent> getAll() {
        return agentRepository.findAll();
    }

    public void update(String agentId, String status, LocalDateTime lastCall, String paused) {
        Agent agent = agentRepository.findAgentByAgentId(agentId);
        agent.setStatus(status);
        agent.setLastCall(lastCall);
        agent.setPaused(paused);
        agentRepository.save(agent);
    }
}
