package com.example.webmorda_backend.service;


import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.repository.WfmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class WfmService {
    WfmRepository wfmRepository;

    boolean existsByAgentidAndAddress(String agentid, String address) {
        return wfmRepository.existsByAgentidAndAddress(agentid, address);
    }

    void addWfm(Wfm wfm) {
        wfmRepository.save(wfm);
    }

    public List<Wfm> findAll() {
        return wfmRepository.findAll();
    }

    Wfm findTopByAgentidOrderByDateDesc(String agentid) {
        return wfmRepository.findTopByAgentidOrderByDateDesc(agentid);
    }

    Wfm getWfmByAgentidAndActionEqualsOrderByDateDesc(String agentid, String action) {
        return wfmRepository.getWfmByAgentidAndActionEqualsOrderByDateDesc(agentid, action);
    }

    public List<Wfm> getWfmByDateBetween(LocalDateTime date, LocalDateTime date2) {
        return wfmRepository.getWfmByDateBetween(date, date2);
    }
    public List<Wfm> getWfmByDateBetweenAndAgentidEquals(LocalDateTime date, LocalDateTime date2, String agentid) {
        return wfmRepository.getWfmByDateBetweenAndAgentidEquals(date, date2, agentid);
    }
}
