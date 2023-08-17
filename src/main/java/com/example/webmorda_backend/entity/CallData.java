package com.example.webmorda_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cdr")
public class CallData {
    @Id
    private String uniqueid;
    private LocalDateTime calldate;
    private String src;
    private String dst;
    private String disposition;
    private int duration;

}
