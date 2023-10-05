package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.service.CallDataService;
import com.example.webmorda_backend.service.WfmService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class WfmController {
    WfmService wfmService;
    CallDataService callDataService;

    @GetMapping("/wfm")
    public ResponseEntity<?> getWfm(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        LocalDateTime[] range = callDataService.getRange(dateTime, dateTime2);
        List<Wfm> res = wfmService.getWfmByDateBetween(range[0], range[1]);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }
}
