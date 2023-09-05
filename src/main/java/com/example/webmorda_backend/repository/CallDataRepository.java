package com.example.webmorda_backend.repository;

import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.model.DispositionCount;
import com.example.webmorda_backend.model.DispositionCountByAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CallDataRepository extends JpaRepository<CallData, String> {
    List<CallData> getCallDataByCalldateBetween(LocalDateTime dateTime, LocalDateTime dateTime2);

    @Query("SELECT new com.example.webmorda_backend.model.DispositionCount(c.disposition,COUNT(*)) FROM CallData c WHERE c.calldate between :startDate AND :endDate GROUP BY c.disposition")
    List<DispositionCount> getCountByDisposition(LocalDateTime startDate, LocalDateTime endDate);

    CallData getCallDataByUniqueid(String uniqueid);
}
