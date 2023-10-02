package com.example.webmorda_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wfm")
public class Wfm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "agentid")
    String agentid;
    @Column(name = "date")
    LocalDateTime date;
    @Column(name = "action")
    String action;
    @Column(name = "address")
    String address;
}
