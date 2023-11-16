package com.example.webmorda_backend.service;

import com.example.webmorda_backend.entity.AgentCallData;
import com.example.webmorda_backend.entity.CallData;
import com.example.webmorda_backend.entity.Wfm;
import com.example.webmorda_backend.model.StatusUpdate;
import lombok.AllArgsConstructor;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;

@Service
@AllArgsConstructor
public class AsteriskAmiService {
    AgentCallDataService agentCallDataService;
    CallDataService callDataService;
    WfmService wfmService;

    SimpMessagingTemplate messagingTemplate;


    public void subscribeToQueueEvents() {
        ManagerConnection amiConnection = new DefaultManagerConnection("172.16.3.185", "aster", "secret");
        try {
            amiConnection.login();
            amiConnection.addEventListener(new ManagerEventListener() {
                @Override
                public void onManagerEvent(ManagerEvent event) {
                    System.out.println(event.toString());
                    if (event instanceof QueueMemberStatusEvent) {
                        String agentId = ((QueueMemberStatusEvent) event).getInterface().substring(4);
                        int statusCode = ((QueueMemberStatusEvent) event).getStatus();
                        String status;
                        switch (statusCode){
                            case 1: {
                                status = "Не используется";
                            }
                            case 2: {
                                status = "В разговоре";
                            }
                            case 5: {
                                status = "Не доступен";
                            }
                            case 8: {
                                status = "На удержаний";
                            }
                            default: {
                                status = "Не доступен";
                            }
                        }

                        messagingTemplate.convertAndSend("/topic/agentStatus", new StatusUpdate(agentId, status));
                    }
                    if (event instanceof VarSetEvent varSetEvent) {
                        String variable = varSetEvent.getVariable();
                        String value = varSetEvent.getValue();
                        String agentId = varSetEvent.getCallerIdNum();
                        LocalDateTime localDateTime = convertToLocalDateTimeViaInstant(varSetEvent.getDateReceived());
                        if ((variable.equals("PQMSTATUS") && value.equals("PAUSED")) || variable.equals("UPQMSTATUS") && value.equals("UNPAUSED")) {
                            if (value.equals("PAUSED")) {
                                Wfm wfm = new Wfm();
                                wfm.setAgentid(agentId);
                                wfm.setDate(localDateTime);
                                wfm.setAction(value);
                                wfmService.addWfm(wfm);
                            } else {
                                Wfm last = wfmService.getWfmByAgentidAndActionEqualsOrderByDateDesc(agentId, "PAUSED");
                                Wfm wfm = new Wfm();
                                wfm.setAgentid(agentId);
                                wfm.setDate(localDateTime);
                                wfm.setAction(value);
                                Duration duration = Duration.between(last.getDate(), localDateTime);
                                wfm.setPausedDuration((int) duration.getSeconds());
                                wfmService.addWfm(wfm);
                            }
                        }
                    }
                    if (event instanceof PeerStatusEvent peerStatusEvent) {
                        String address = peerStatusEvent.getAddress();
                        String agentID = peerStatusEvent.getPeer().substring(4, 8);
                        String status = peerStatusEvent.getPeerStatus();
                        Date date = peerStatusEvent.getDateReceived();
                        LocalDateTime localDateTime = convertToLocalDateTimeViaInstant(date);
                        if (status.equals("Unregistered")) {
                            Wfm wfm = new Wfm();
                            wfm.setAgentid(agentID);
                            wfm.setAction("Logout");
                            wfm.setAddress(address);
                            wfm.setDate(localDateTime);
                            wfmService.addWfm(wfm);
                        } else if (!wfmService.existsByAgentidAndAddress(agentID, address)) {
                            Wfm wfm = new Wfm();
                            wfm.setAgentid(agentID);
                            wfm.setAction("Login");
                            wfm.setAddress(address);
                            wfm.setDate(localDateTime);
                            wfmService.addWfm(wfm);
                        }
                    }
                    if (event instanceof MonitorStartEvent monitorStartEvent) {
                        CallData callData = new CallData();
                        String source = monitorStartEvent.getCallerIdNum();
                        callData.setUniqueid(monitorStartEvent.getUniqueId());
                        callData.setCalldate(convertToLocalDateTimeViaInstant(monitorStartEvent.getDateReceived()));
                        callData.setLanguage(source.substring(0, 2));
                        callData.setSrc(source.substring(2));
                        callData.setDisposition("NO ANSWER");
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
                            if (!agentCallDataService.existsByCalldataidAndAgentidAndDisposition(calldataid, agent, status) && !(status.equals("NOANSWER") || status.equals("CANCEL"))) {
                                addNewAgenCallData(calldataid, agent, status);
                            }
                        }
                    }
                    if (event instanceof AgentRingNoAnswerEvent agentRingNoAnswerEvent) {
                        String agent = agentRingNoAnswerEvent.getInterface().substring(4, 8);
                        String status = "NOANSWER";
                        String calldataid = agentRingNoAnswerEvent.getUniqueId();
                        if (!agentCallDataService.existsByCalldataidAndAgentidAndDisposition(calldataid, agent, status)) {
                            addNewAgenCallData(calldataid, agent, status);
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
                        Duration durationConsult = Duration.between(callData.getConnect(), finish);
                        callData.setDurationConsult((int) durationConsult.getSeconds());
                        callData.setDisconnect(finish);
                        callData.setAudio_path("/var/spool/asterisk/monitor/" + id + "-in.wav");
                        callDataService.add(callData);
                    }
                    if (event instanceof QueueCallerAbandonEvent queueCallerAbandonEvent) {
                        String id = queueCallerAbandonEvent.getLinkedId();
                        CallData callData = callDataService.getCallDataByUniqueId(id);
                        callData.setDisposition("CANCEL");
                        callDataService.add(callData);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNewAgenCallData(String calldataid, String agent, String status) {
        AgentCallData agentCallData = new AgentCallData();
        CallData callData = callDataService.getCallDataByUniqueId(calldataid);
        agentCallData.setCalldate(callData.getCalldate());
        agentCallData.setAgentid(agent);
        agentCallData.setDisposition(status);
        agentCallData.setCalldataid(calldataid);
        agentCallDataService.add(agentCallData);
    }

    public LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
