package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.AgentCallData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentCallDataRepository extends JpaRepository<AgentCallData, Long> {
    AgentCallData findAgentCallDataByCalldataidAndDispositionEquals(String calldataid, String disposition);
}
