package kr.inhatc.spring.excel.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.excel.entity.ExcelTutorial;
import kr.inhatc.spring.excel.repository.ExcelTutorialRepository;
import kr.inhatc.spring.excel.utils.ExcelHelper;

@Service
public class ExcelDataService {
  @Autowired
  ExcelTutorialRepository repository;

  /**
   * 엑셀 파일 저장하기
   * 
   * @param file
   */
  public void save(MultipartFile file) {
    try {
      List<ExcelTutorial> excelList = ExcelHelper.excelToTutorials(file.getInputStream());
      repository.saveAll(excelList);
    } catch (IOException e) {
      throw new RuntimeException("엑셀 데이터 저장시 오류 발생 : " + e.getMessage());
    }
  }

  /**
   * 모든 엑셀 가져오기
   * 
   * @return
   */
  public List<ExcelTutorial> getAllExcelTutorials() {
    return repository.findAll();
  }

  public ByteArrayInputStream load() {
    List<ExcelTutorial> excels = repository.findAll();
    ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(excels);
    return in;
  }

}
