package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.service.WfmService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class WfmController {
    WfmService wfmService;

    @GetMapping("/wfm")
    public ResponseEntity<?> getWfm() {
        List<Wfm> res = wfmService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
