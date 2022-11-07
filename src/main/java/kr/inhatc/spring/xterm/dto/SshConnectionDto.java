package kr.inhatc.spring.xterm.dto;

import org.springframework.web.socket.WebSocketSession;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;

import lombok.Data;

@Data
public class SshConnectionDto {
    
    private WebSocketSession session;
    private JSch jsch;
    private Channel channel;
    private SshDto info;

}