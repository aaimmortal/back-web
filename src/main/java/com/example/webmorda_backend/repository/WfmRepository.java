package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.Wfm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WfmRepository extends JpaRepository<Wfm, Long> {

    boolean existsByAgentidAndAddress(String agentid, String address);
    Wfm findTopByAgentidOrderByDateDesc(String agentid);
    Wfm getWfmByAgentidAndActionEqualsOrderByDateDesc(String agentid, String action);
    List<Wfm> getWfmByDateBetween(LocalDateTime date, LocalDateTime date2);
    List<Wfm> getWfmByDateBetweenAndAgentidEquals(LocalDateTime date, LocalDateTime date2, String agentid);

}
