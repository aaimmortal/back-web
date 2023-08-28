package com.example.webmorda_backend.service;


import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@AllArgsConstructor
public class StartupService {
    private AsteriskAmiService amiService;

    @PostConstruct
    public void init() {
        amiService.subscribeToQueueEvents();
    }
}