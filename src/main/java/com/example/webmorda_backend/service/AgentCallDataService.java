package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import com.example.webmorda_backend.repository.AgentCallDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgentCallDataService {
    AgentCallDataRepository agentCallDataRepository;

    public void add(AgentCallData agentCallData) {
        agentCallDataRepository.save(agentCallData);
    }

    public List<DispositionCountByAccount> getDispositionCountByAccount(LocalDateTime startDate, LocalDateTime endDate) {
        return agentCallDataRepository.getDispositionCountByAccount(startDate, endDate);
    }

    public boolean existsByCalldataidAndAgentidAndDisposition(String id, String agentid, String disposition) {
        return agentCallDataRepository.existsByCalldataidAndAgentidAndDisposition(id, agentid, disposition);
    }

    public List<AgentCallData> getAgentCallDataByCallDataID(String id) {
        return agentCallDataRepository.findAgentCallDataByCalldataid(id);
    }
}
