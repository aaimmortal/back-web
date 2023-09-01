package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import com.example.webmorda_backend.service.AgentCallDataService;
import com.example.webmorda_backend.service.AsteriskAmiService;
import com.example.webmorda_backend.service.CallDataService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class CallDataController {
    CallDataService callDataService;
    AgentCallDataService agentCallDataService;
    AsteriskAmiService asteriskAmiService;

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

    @GetMapping("/callData")
    public ResponseEntity<?> callData(@RequestParam("callDataId") String callDataId) {
        CallData callData = callDataService.getCallDataByUniqueId(callDataId);
        if (callData == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No data");
        return ResponseEntity.status(HttpStatus.OK).body(callData);
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
        List<DispositionCountByAccount> res = agentCallDataService.getDispositionCountByAccount();
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

    @GetMapping("/getAudioBetween")
    public ResponseEntity<?> getAudioBetween(@RequestParam("dateTime") String dateTime, @RequestParam("dateTime2") String dateTime2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime1 = LocalDateTime.parse(dateTime, formatter);
        LocalDateTime localDateTime2 = LocalDateTime.parse(dateTime2, formatter);
        List<CallData> res = callDataService.getCallDataByCalldateBetween(localDateTime1, localDateTime2);
        if (res.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK).body("No data");
        }
        for (CallData re : res) {
            if (!re.getAudio_path().equals("")) {
                Path path = Paths.get(re.getAudio_path());
                byte[] fileContent = Files.readAllBytes(path);
                ZipEntry zipEntry = new ZipEntry(re.getAudio_path());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(fileContent);
                zipOutputStream.closeEntry();
            }
        }
        zipOutputStream.close();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        byte[] zipBytes = byteArrayOutputStream.toByteArray();
        ByteArrayResource resource = new ByteArrayResource(zipBytes);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resource);
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

