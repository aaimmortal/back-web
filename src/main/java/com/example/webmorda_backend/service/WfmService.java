package com.example.webmorda_backend.service;


import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.repository.WfmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    Wfm findTopByAgentidOrderByDate(String agentid) {
        return wfmRepository.findTopByAgentidOrderByDate(agentid);
    }
}
