package com.example.webmorda_backend.model;

import lombok.Data;

@Data
public class DispositionCountByAccount {
    private String src;
    private String disposition;
    private Long count;

    public DispositionCountByAccount(String src, String disposition, Long count) {
        this.src = src;
        this.disposition = disposition;
        this.count = count;
    }
}
