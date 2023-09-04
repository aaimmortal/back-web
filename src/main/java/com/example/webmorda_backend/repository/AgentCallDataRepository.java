package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentCallDataRepository extends JpaRepository<AgentCallData, Long> {
    @Query("SELECT new com.example.webmorda_backend.model.DispositionCountByAccount(c.agentid,c.disposition,COUNT(*))FROM AgentCallData c GROUP BY c.agentid, c.disposition")
    List<DispositionCountByAccount> getDispositionCountByAccount();
    boolean existsByCalldataidAndAgentidAndDisposition(String calldataid, String agentid, String disposition);
    List<AgentCallData> findAgentCallDataByCalldataid(String calldataid);

}
