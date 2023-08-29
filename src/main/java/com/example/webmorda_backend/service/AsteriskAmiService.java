package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.repository.AgentCallDataRepository;
import lombok.AllArgsConstructor;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class AsteriskAmiService {
    AgentCallDataService agentCallDataService;
    AgentCallDataRepository agentCallDataRepository;

    public void subscribeToQueueEvents() {
        ManagerConnection amiConnection = new DefaultManagerConnection("172.16.3.185", "aster", "secret");
        try {
            amiConnection.login();
            amiConnection.addEventListener(new ManagerEventListener() {
                @Override
                public void onManagerEvent(ManagerEvent event) {
                    System.out.println(event);
                    if (event instanceof DialEvent dialEvent) {
                        if (dialEvent.getSubEvent().equals("End")) {
                            if (dialEvent.getDialStatus() != null) {
                                String agent = dialEvent.getDestination().substring(4, 8);
                                String status = dialEvent.getDialStatus();
                                String calldataid = dialEvent.getUniqueId();
                                AgentCallData agentCallData;
                                if (!agentCallDataService.existsByCallDataId(calldataid, agent)) {
                                    agentCallData = new AgentCallData();
                                    agentCallData.setAgentid(agent);
                                    agentCallData.setDisposition(status);
                                    agentCallData.setCalldataid(calldataid);
                                    agentCallDataService.add(agentCallData);
                                }
                            }
                        }
                    }
                    if (event instanceof AgentConnectEvent agentConnectEvent) {
                        String id = agentConnectEvent.getLinkedId();
                        AgentCallData agentCallData = agentCallDataService.getAgentCalldataByCalldataid(id);
                        agentCallData.setConnect(convertToLocalDateTimeViaInstant(agentConnectEvent.getDateReceived()));
                        agentCallDataService.add(agentCallData);
                    }
                    if (event instanceof AgentCompleteEvent agentCompleteEvent) {
                        String id = agentCompleteEvent.getLinkedId();
                        AgentCallData agentCallData = agentCallDataService.getAgentCalldataByCalldataid(id);
                        agentCallData.setDisconnect(convertToLocalDateTimeViaInstant(agentCompleteEvent.getDateReceived()));
                        agentCallDataService.add(agentCallData);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
