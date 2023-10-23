package com.example.webmorda_backend.controller;

import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.service.CallDataService;
import com.example.webmorda_backend.service.WfmService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/wfmGraph")
    public ResponseEntity<?> wfmGraph(@RequestParam("date") String date, @RequestParam("agents") String agents) {
        String dateTime = date + " 00:00:00";
        String dateTime2 = date + " 23:59:59";
        LocalDateTime[] range = callDataService.getRange(dateTime, dateTime2);
        List<List<Wfm>> res = new ArrayList<>();
        String[] agentids = agents.split(" ");
        for (String agentid: agentids) {
            List<Wfm> temp = wfmService.getWfmByDateBetweenAndAgentidEquals(range[0], range[1], agentid);
            res.add(temp);
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
