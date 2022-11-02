package kr.inhatc.spring.excel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.inhatc.spring.excel.entity.ExcelData;

public interface ExcelDataRepository extends JpaRepository<ExcelData, Long> {

}
