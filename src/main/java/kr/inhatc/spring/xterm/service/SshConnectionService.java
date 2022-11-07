package kr.inhatc.spring.xterm.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import kr.inhatc.spring.xterm.dto.SshConnectionDto;
import kr.inhatc.spring.xterm.dto.SshDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SshConnectionService {
  private static Map<WebSocketSession, Object> sshMap = new ConcurrentHashMap<>();
  
  private ExecutorService executorService = Executors.newCachedThreadPool();

  public void initConnection(WebSocketSession session, SshDto dto) {
      JSch jsch = new JSch();
      SshConnectionDto connectionDto = new SshConnectionDto();
      connectionDto.setJsch(jsch);
      connectionDto.setSession(session);
      connectionDto.setInfo(dto);
      sshMap.put(session, connectionDto);
      
      executorService.execute(new Runnable() {
          @Override
          public void run() {
              try {
                  connectToSSH(connectionDto, dto, session);
              } catch (JSchException | IOException e) {
                  log.error("에러 정보: {}", e);
                  close(session);
              }
          }
      });
  }
  
  public void recvHandle(WebSocketSession session, String command) {
      SshConnectionDto connectionDto = (SshConnectionDto) sshMap.get(session);
      
      if (connectionDto != null) {
          try {
              transToSSh(connectionDto.getChannel(), command);
          } catch (IOException e) {
              log.error("에러 정보: {}", e);
              close(session);
          }
      }
  }
  
  public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
      session.sendMessage(new TextMessage(buffer));
  }
  
  public void close(WebSocketSession session) {
      SshConnectionDto connectionDto = (SshConnectionDto) sshMap.get(session);
      if (connectionDto != null) {
          if (connectionDto.getChannel() != null) connectionDto.getChannel().disconnect();
          sshMap.remove(session);
      }
  }
  
  private void connectToSSH(SshConnectionDto connectionDto, SshDto dto, WebSocketSession webSocketSession) throws JSchException, IOException {
      Session session = null;
      Properties config = new Properties();
      config.put("StrictHostKeyChecking", "no");
      
      session = connectionDto.getJsch().getSession(dto.getUsername(), dto.getHost(), dto.getPort());
      session.setConfig(config);
      
      session.setPassword(dto.getPassword());
      session.connect(60000);
      
      Channel channel = session.openChannel("shell");
      
      channel.connect(3000);
      connectionDto.setChannel(channel);
      
      InputStream is = channel.getInputStream();
      try {
          byte[] buffer = new byte[1024];
          int i = 0;
          while((i = is.read(buffer)) != -1) {
              sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
          }
      } finally {
          session.disconnect();
          channel.disconnect();
          if (is != null) {
              is.close();
          }
      }
  }
  
  private void transToSSh(Channel channel, String command) throws IOException {
      if (channel != null) {
          OutputStream os = channel.getOutputStream();
          if (command.equals("SIGINT")) {
              os.write(3);
          } else if(command.equals("SIGTSTP")) {
              os.write(26);
          } else {
              os.write(command.getBytes());
          }
          os.flush();
      }
  }

}