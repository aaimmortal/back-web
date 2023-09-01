package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.entity.CallData;
import lombok.AllArgsConstructor;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class AsteriskAmiService {
    AgentCallDataService agentCallDataService;
    CallDataService callDataService;

    public void subscribeToQueueEvents() {
        ManagerConnection amiConnection = new DefaultManagerConnection("172.16.3.185", "aster", "secret");
        try {
            amiConnection.login();
            amiConnection.addEventListener(new ManagerEventListener() {
                @Override
                public void onManagerEvent(ManagerEvent event) {
                    System.out.println(event);
                    if (event instanceof MonitorStartEvent monitorStartEvent) {
                        CallData callData = new CallData();
                        callData.setUniqueid(monitorStartEvent.getUniqueId());
                        callData.setCalldate(convertToLocalDateTimeViaInstant(monitorStartEvent.getDateReceived()));
                        callData.setSrc(monitorStartEvent.getCallerIdNum());
                        callData.setDisposition("NO ANSWER");
                        callDataService.add(callData);
                    }
                    if (event instanceof MonitorStopEvent monitorStopEvent) {
                        String id = monitorStopEvent.getUniqueId();
                        LocalDateTime finish = convertToLocalDateTimeViaInstant(monitorStopEvent.getDateReceived());
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        Duration duration = Duration.between(callData.getCalldate(), finish);
                        callData.setDuration((int) duration.getSeconds());
                        callDataService.add(callData);
                    }
                    if (event instanceof DialEvent dialEvent) {
                        if (dialEvent.getSubEvent().equals("End")) {
                            if (dialEvent.getDialStatus() != null) {
                                String agent = dialEvent.getDestination().substring(4, 8);
                                String status = dialEvent.getDialStatus();
                                String calldataid = dialEvent.getUniqueId();
                                AgentCallData agentCallData;
                                agentCallData = new AgentCallData();
                                agentCallData.setAgentid(agent);
                                agentCallData.setDisposition(status);
                                agentCallData.setCalldataid(calldataid);
                                agentCallDataService.add(agentCallData);
                            }
                        }
                    }
                    if (event instanceof AgentConnectEvent agentConnectEvent) {
                        String id = agentConnectEvent.getLinkedId();
                        AgentCallData agentCallData = agentCallDataService.getAgentCalldataByCalldataid(id);
                        agentCallData.setConnect(convertToLocalDateTimeViaInstant(agentConnectEvent.getDateReceived()));
                        agentCallDataService.add(agentCallData);
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        callData.setDst(agentConnectEvent.getMemberName().substring(4, 8));
                        callData.setDisposition("ANSWERED");
                        callDataService.add(callData);
                    }
                    if (event instanceof AgentCompleteEvent agentCompleteEvent) {
                        String id = agentCompleteEvent.getLinkedId();
                        AgentCallData agentCallData = agentCallDataService.getAgentCalldataByCalldataid(id);
                        LocalDateTime finish = convertToLocalDateTimeViaInstant(agentCompleteEvent.getDateReceived());
                        agentCallData.setDisconnect(finish);
                        agentCallDataService.add(agentCallData);
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        callData.setAudio_path("/var/spool/asterisk/monitor/" + id + "-in.wav");
                        callDataService.add(callData);
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
