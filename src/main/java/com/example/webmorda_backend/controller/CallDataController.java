package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import com.example.webmorda_backend.service.AgentCallDataService;
import com.example.webmorda_backend.service.CallDataService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class CallDataController {
    CallDataService callDataService;
    AgentCallDataService agentCallDataService;

    @GetMapping("/calldateBetween")
    public ResponseEntity<?> getCallDataBetween(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        LocalDateTime[] range = callDataService.getRange(dateTime, dateTime2);
        List<CallData> res = callDataService.getCallDataByCalldateBetween(range[0], range[1]);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/callData")
    public ResponseEntity<?> callData(@RequestParam("callDataId") String callDataId) {
        CallData callData = callDataService.getCallDataByUniqueId(callDataId);
        if (callData == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        return ResponseEntity.status(HttpStatus.OK).body(callData);
    }

    @GetMapping("/dispositionCount")
    public ResponseEntity<?> getCountByDisposition(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        LocalDateTime[] range = callDataService.getRange(dateTime, dateTime2);
        List<DispositionCount> res = callDataService.getCountByDisposition(range[0], range[1]);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/dispositionCountByAccount")
    public ResponseEntity<?> getDispositionCountByAccount(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) {
        LocalDateTime[] range = callDataService.getRange(dateTime, dateTime2);
        List<DispositionCountByAccount> res = agentCallDataService.getDispositionCountByAccount(range[0], range[1]);
        if (res != null) {
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }

    @GetMapping("/audio")
    public ResponseEntity<?> getAudio(@RequestParam("id") String id) throws IOException {
        Path filePath = Paths.get(callDataService.getCallDataByUniqueId(id).getAudio_path());
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/agentCallData")
    public ResponseEntity<?> agentCallData(@RequestParam("callDataId") String callDataId) {
        List<AgentCallData> agentCallDataList = agentCallDataService.getAgentCallDataByCallDataID(callDataId);
        if (agentCallDataList != null) {
            return ResponseEntity.status(HttpStatus.OK).body(agentCallDataList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        }
    }
}

