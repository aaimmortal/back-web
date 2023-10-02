package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.Wfm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WfmRepository extends JpaRepository<Wfm, Long> {

    boolean existsByAgentidAndAddress(String agentid, String address);

}
