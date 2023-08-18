package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import com.example.webmorda_backend.service.CallDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class CallDataController {
    CallDataService callDataService;

    @GetMapping("/calldateBetween")
    public ResponseEntity<?> getCallDataBetween(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = LocalDateTime.parse(dateTime, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateTime2, formatter);
        List<CallData> res = callDataService.getCallDataByCalldateBetween(localDateTime1, localDateTime2);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/dispositionCount")
    public ResponseEntity<?> getCountByDisposition() {
        List<DispositionCount> res = callDataService.getCountByDisposition();
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/getAllCalldata")
    public ResponseEntity<?> getAllCalldata() {
        List<CallData> res = callDataService.getAllCalldata();
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/dispositionCountByAccount")
    public ResponseEntity<?> getDispositionCountByAccount() {
        List<DispositionCountByAccount> res = callDataService.getDispositionCountByAccount();
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/getAudioBetween")
    public ResponseEntity<?> getAudioBetween(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = LocalDateTime.parse(dateTime, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateTime2, formatter);
        List<CallData> res = callDataService.getCallDataByCalldateBetween(localDateTime1, localDateTime2);
        List<String> audio = new ArrayList<>();
        for (CallData re : res) {
            audio.add(re.getAudio_path());
        }
        if (audio.size() != 0) {
            return ResponseEntity.status(HttpStatus.OK).body(audio);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }
}

