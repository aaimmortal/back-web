package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.repository.CallDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class CallDataService {
    CallDataRepository callDataRepository;

    public List<CallData> getCallDataByCalldateBetween(LocalDateTime dateTime, LocalDateTime dateTime2) {
        return callDataRepository.getCallDataByCalldateBetween(dateTime, dateTime2);
    }

    public CallData getCallDataByUniqueId(String id) {
        return callDataRepository.getCallDataByUniqueid(id);
    }

    public List<DispositionCount> getCountByDisposition() {
        return callDataRepository.getCountByDisposition();
    }

    public List<CallData> getAllCalldata() {
        return callDataRepository.findAll();
    }

}
