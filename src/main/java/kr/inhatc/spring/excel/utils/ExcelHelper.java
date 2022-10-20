package kr.inhatc.spring.excel.utils;

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

import kr.inhatc.spring.excel.entity.ExcelTutorial;

/**
 * 엑셀 파일을 읽고 쓰는데 도움을 주는 클래스 
 * @author 김기태
 *
 */
public class ExcelHelper
{
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	public static String SHEET = "Sheet1";

	public static boolean hasExcelFormat(MultipartFile file)
	{
		if (!TYPE.equals(file.getContentType()))
		{
			return false;
		}
		return true;
	}

	public static List<ExcelTutorial> excelToTutorials(InputStream is)
	{
		try
		{
			Workbook workbook = new XSSFWorkbook(is);
			Sheet sheet = workbook.getSheet(SHEET);
			Iterator<Row> rows = sheet.iterator();
			List<ExcelTutorial> excelList = new ArrayList<ExcelTutorial>();
			int rowNumber = 0;
			
			while (rows.hasNext())
			{
				Row currentRow = rows.next();
				
				// 스킵 헤더 
				if (rowNumber == 0)
				{
					rowNumber++;
					continue;
				}
			
				Iterator<Cell> cellsInRow = currentRow.iterator();
				ExcelTutorial excel = new ExcelTutorial();
				int cellIdx = 0;

				while (cellsInRow.hasNext())
				{
					Cell currentCell = cellsInRow.next();
				
					switch (cellIdx)
					{
					case 0:
						excel.setId((long) currentCell.getNumericCellValue());
						break;
					case 1:
						excel.setTitle(currentCell.getStringCellValue());
						break;
					case 2:
						excel.setDescription(currentCell.getStringCellValue());
						break;
					case 3:
						excel.setPublished(currentCell.getBooleanCellValue());
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
		} 
		catch (IOException e)
		{
			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}
	}
}
