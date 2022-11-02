package kr.inhatc.spring.excel.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExcelData {

  @Id
  @Column(name = "id")
  private Long id;
  
  private String name;
  
  private String position;
  
  private String office;
  
  private int age;
  
  private LocalDateTime startDate;
  
  private int saraly;
}
