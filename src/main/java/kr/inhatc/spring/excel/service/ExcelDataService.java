package kr.inhatc.spring.excel.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.excel.entity.ExcelData;
import kr.inhatc.spring.excel.entity.ExcelTutorial;
import kr.inhatc.spring.excel.repository.ExcelDataRepository;
import kr.inhatc.spring.excel.repository.ExcelTutorialRepository;
import kr.inhatc.spring.excel.utils.ExcelDataHelper;
import kr.inhatc.spring.excel.utils.ExcelHelper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExcelDataService {
  @Autowired
  ExcelDataRepository repository;

  /**
   * 엑셀 파일 저장하기
   * 
   * @param file
   */
  @Transactional
  public void save(MultipartFile file) {
    try {
      repository.deleteAll();
      List<ExcelData> excelList = ExcelDataHelper.excelToExcelData(file.getInputStream());
      repository.saveAll(excelList);
      log.info(">>>> 리스트 크기 : " + excelList.size());
    } catch (IOException e) {
      throw new RuntimeException("엑셀 데이터 저장시 오류 발생 : " + e.getMessage());
    }
  }

  /**
   * 모든 엑셀 가져오기
   * 
   * @return
   */
  public List<ExcelData> getAllExcelDatas() {
    return repository.findAll();
  }

  public ByteArrayInputStream load() {
    List<ExcelData> excels = repository.findAll();
    ByteArrayInputStream in = ExcelDataHelper.excelDataToExcel(excels);
    return in;
  }
  

}
