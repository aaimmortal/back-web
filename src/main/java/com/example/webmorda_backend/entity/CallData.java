package com.example.webmorda_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "calldata")
public class CallData {
    @Id
    @Column(name = "id")
    private String uniqueid;
    @Column(name = "calldate")
    private LocalDateTime calldate;
    @Column(name = "source")
    private String src;
    @Column(name = "destination")
    private String dst;
    @Column(name = "disposition")
    private String disposition;
    @Column(name = "duration")
    private int duration;
    @Column(name = "audiopath")
    private String audio_path;
    @Column(name = "connect")
    private LocalDateTime connect;
    @Column(name = "disconnect")
    private LocalDateTime disconnect;
    @Column(name = "rating")
    private int rating;
    @Column(name = "waiting")
    private int waiting;
    @Column(name = "durationconsult")
    private int durationConsult;
    @Column(name = "language")
    String language;
}
