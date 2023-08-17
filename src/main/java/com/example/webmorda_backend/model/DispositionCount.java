package com.example.webmorda_backend.model;

import lombok.Data;

@Data
public class DispositionCount {
    private String disposition;
    private Long count;
    public DispositionCount(String disposition, Long count) {
        this.disposition = disposition;
        this.count = count;
    }

}
