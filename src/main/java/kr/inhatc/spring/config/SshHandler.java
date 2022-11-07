package kr.inhatc.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.inhatc.spring.xterm.dto.SshDto;
import kr.inhatc.spring.xterm.service.SshConnectionService;
import kr.inhatc.spring.xterm.service.SshService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SshHandler implements WebSocketHandler { 
    
    @Autowired
    private SshService service;
    
    @Autowired
    private SshConnectionService conService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 웹 소켓 연결      
        log.info("{} 연결됨", session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message.getPayload().toString().contains("OPEN WEB SOCKET")) {
            ObjectMapper om = new ObjectMapper();
            SshDto dto = om.readValue(message.getPayload().toString(), SshDto.class);
            dto = service.getHostInfo(dto);
            conService.initConnection(session, dto);
        }
        
        conService.recvHandle(session, message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        conService.close(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        // TODO Auto-generated method stub
        return false;
    }
    
}