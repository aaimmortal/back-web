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
                        String source = monitorStartEvent.getCallerIdNum();
                        callData.setUniqueid(monitorStartEvent.getUniqueId());
                        callData.setCalldate(convertToLocalDateTimeViaInstant(monitorStartEvent.getDateReceived()));
                        callData.setLanguage(source.substring(0, 2));
                        callData.setSrc(source.substring(2));
                        callData.setDisposition("CANCEL");
                        callDataService.add(callData);
                    }
                    if (event instanceof MonitorStopEvent monitorStopEvent) {
                        String id = monitorStopEvent.getUniqueId();
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        LocalDateTime finish = convertToLocalDateTimeViaInstant(monitorStopEvent.getDateReceived());
                        Duration duration = Duration.between(callData.getCalldate(), finish);
                        callData.setDuration((int) duration.getSeconds());
                        callDataService.add(callData);
                    }
                    if (event instanceof DialEvent dialEvent) {
                        if (dialEvent.getDialStatus() != null && dialEvent.getSubEvent().equals("End")) {
                            String agent = dialEvent.getDestination().substring(4, 8);
                            String status = dialEvent.getDialStatus();
                            String calldataid = dialEvent.getUniqueId();
                            if (!agentCallDataService.existsByCalldataidAndAgentidAndDisposition(calldataid, agent, status)) {
                                AgentCallData agentCallData = new AgentCallData();
                                agentCallData.setAgentid(agent);
                                agentCallData.setDisposition(status);
                                agentCallData.setCalldataid(calldataid);
                                agentCallDataService.add(agentCallData);
                            }
                        }
                    }
                    if (event instanceof AgentConnectEvent agentConnectEvent) {
                        String id = agentConnectEvent.getLinkedId();
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        LocalDateTime start = convertToLocalDateTimeViaInstant(agentConnectEvent.getDateReceived());
                        Duration waiting = Duration.between(callData.getCalldate(), start);
                        callData.setConnect(start);
                        callData.setWaiting((int) waiting.getSeconds());
                        callData.setDst(agentConnectEvent.getMemberName().substring(4, 8));
                        callData.setDisposition("ANSWERED");
                        callDataService.add(callData);
                    }
                    if (event instanceof AgentCompleteEvent agentCompleteEvent) {
                        String id = agentCompleteEvent.getLinkedId();
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        LocalDateTime finish = convertToLocalDateTimeViaInstant(agentCompleteEvent.getDateReceived());
                        Duration dusrationConsult = Duration.between(callData.getConnect(), finish);
                        callData.setDurationConsult((int) dusrationConsult.getSeconds());
                        callData.setDisconnect(finish);
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
