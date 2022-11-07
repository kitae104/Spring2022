package kr.inhatc.spring.xterm.dto;

import lombok.Data;

@Data
public class SshDto {
  
  private String type;
  private String host;
  private int port;
  private String username;
  private String password;
}
