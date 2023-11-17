package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findAgentByAgentId(String agentId);
}
