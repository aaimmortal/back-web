package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.repository.CallDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CallDataService {
    CallDataRepository callDataRepository;

    public List<CallData> getCallDataByCalldateBetween(LocalDateTime dateTime, LocalDateTime dateTime2) {
        return callDataRepository.getCallDataByCalldateBetween(dateTime, dateTime2);
    }

    public void add(CallData callData) {
        callDataRepository.save(callData);
    }

    public CallData getCallDataByUniqueId(String id) {
        return callDataRepository.getCallDataByUniqueid(id);
    }

    public List<DispositionCount> getCountByDisposition(LocalDateTime startDate, LocalDateTime endDate) {
        return callDataRepository.getCountByDisposition(startDate, endDate);
    }

    public List<CallData> getAllCalldata() {
        return callDataRepository.findAll();
    }
    public LocalDateTime[] getRange(String dateTime, String dateTime2) {
        LocalDateTime localDateTime1, localDateTime2;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (dateTime.equals("") && dateTime2.equals("")) {
            localDateTime1 = LocalDateTime.MIN;
            localDateTime2 = LocalDateTime.now();
        } else {
            localDateTime1 = LocalDateTime.parse(dateTime, formatter);
            localDateTime2 = LocalDateTime.parse(dateTime2, formatter);
        }
        return new LocalDateTime[]{localDateTime1, localDateTime2};
    }

}
