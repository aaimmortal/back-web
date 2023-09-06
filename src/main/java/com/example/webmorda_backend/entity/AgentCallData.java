package com.example.webmorda_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "agent_calldata")
public class AgentCallData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "calldataid")
    private String calldataid;
    @Column(name = "agentid")
    private String agentid;
    @Column(name = "disposition")
    private String disposition;
    @Column(name = "calldate")
    LocalDateTime calldate;
}
