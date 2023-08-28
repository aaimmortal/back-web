package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.repository.AgentCallDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AgentCallDataService {
    AgentCallDataRepository agentCallDataRepository;

    public void add(AgentCallData agentCallData) {
        agentCallDataRepository.save(agentCallData);
    }

    public AgentCallData getAgentCalldataByCalldataid(String id) {
        return agentCallDataRepository.findAgentCallDataByCalldataidAndDispositionEquals(id,"ANSWER");
    }

}
