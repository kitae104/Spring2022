package kr.inhatc.spring.excel.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.excel.entity.ExcelData;

/**
 * 엑셀 파일을 읽고 쓰는데 도움을 주는 클래스
 * 
 * @author 김기태
 *
 */
public class ExcelDataHelper {
 
  // 설정 정보(HEADERs 정보를 엑셀 파일에 맞게 변경)
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static String[] HEADERs = { "Id", "Name", "Position", "Office", "Age", "Start date", "Salary" };
  public static String SHEET = "Sheet1";

  /**
   * 엑셀 포맷이 맞는지 확인
   * 
   * @param file
   * @return
   */
  public static boolean hasExcelFormat(MultipartFile file) {
    if (!TYPE.equals(file.getContentType())) {
      return false;
    }
    return true;
  }

  /**
   * 엑셀 파일을 데이터 베이스로 변환
   * 
   * @param inputStream
   * @return
   */
  public static List<ExcelData> excelToExcelData(InputStream inputStream) {
    try {
      Workbook workbook = new XSSFWorkbook(inputStream);
      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();
      List<ExcelData> excelList = new ArrayList<ExcelData>();
      int rowNumber = 0;

      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // 스킵 헤더
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();
        ExcelData excel = new ExcelData();
        int cellIdx = 0;

        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
            case 0:
              excel.setId((long) currentCell.getNumericCellValue()); // 숫자
              break;
            case 1:
              excel.setName(currentCell.getStringCellValue()); // 문자
              break;
            case 2:
              excel.setPosition(currentCell.getStringCellValue());
              break;
            case 3:
              excel.setOffice(currentCell.getStringCellValue());
              break;
            case 4:
              excel.setAge((int)currentCell.getNumericCellValue());
              break;
            case 5:
              excel.setStartDate(currentCell.getLocalDateTimeCellValue());
              break;
            case 6:
              excel.setSaraly((int)currentCell.getNumericCellValue());
              break;  
            default:
              break;
          }
          cellIdx++;
        }
        excelList.add(excel);
      }
      workbook.close();
      return excelList;
    } catch (IOException e) {
      throw new RuntimeException("Excel 파일을 읽어오는데 실패 : " + e.getMessage());
    }
  }

  /**
   * 데이터 베이스 정보를 이용하여 엑셀파일 만들기
   * 
   * @param tutorials
   * @return
   */
  public static ByteArrayInputStream excelDataToExcel(List<ExcelData> excelDatas) {
    try (Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();) {
      
      // 시트 설정 
      Sheet sheet = workbook.createSheet(SHEET);

      // Header 설정 
      Row headerRow = sheet.createRow(0);

      // Header 항목 설정 
      for (int col = 0; col < HEADERs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(HEADERs[col]);
      }

      int rowIdx = 1;
      for (ExcelData excelData : excelDatas) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(excelData.getId());
        row.createCell(1).setCellValue(excelData.getName());
        row.createCell(2).setCellValue(excelData.getPosition());
        row.createCell(3).setCellValue(excelData.getOffice());
        row.createCell(4).setCellValue(excelData.getAge());
        row.createCell(5).setCellValue(excelData.getStartDate());
        row.createCell(6).setCellValue(excelData.getSaraly());
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("엑셀로 데이터 가져오기 실패 : " + e.getMessage());
    }
  }
}
