package kr.inhatc.spring.excel.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ExcelDataDto {
  
  private Long id;
  
  private String name;
  
  private String position;
  
  private String office;
  
  private int age;
  
  private LocalDateTime startDate;
  
  private int saraly;
}
