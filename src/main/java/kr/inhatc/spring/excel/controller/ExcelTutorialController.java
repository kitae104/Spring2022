package kr.inhatc.spring.excel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.excel.entity.ExcelTutorial;
import kr.inhatc.spring.excel.service.ExcelTutorialService;
import kr.inhatc.spring.excel.utils.ExcelHelper;
import kr.inhatc.spring.excel.utils.ExcelResponseMessage;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class ExcelTutorialController
{
	@Autowired
	ExcelTutorialService fileService;

	@GetMapping(value = "/excel/excelUpload")
	public String excelUpload() {
		return "excel/excelUpload"; 
	}
	
	@PostMapping("/excel/upload")
	public @ResponseBody ResponseEntity uploadFile(@RequestParam("file") MultipartFile file)	
	{		
		log.info("================================= 엑셀 업로드 처리");	
		
		String message = "";
		if (ExcelHelper.hasExcelFormat(file))
		{
			try
			{
				fileService.save(file);
				List<ExcelTutorial> excelList = fileService.getAllExcelTutorials();
				if (excelList.isEmpty())
				{
					return new ResponseEntity<String>(message, HttpStatus.NO_CONTENT);
				}
				return new ResponseEntity<List<ExcelTutorial>>(excelList, HttpStatus.OK);
			} 
			catch (Exception e)
			{
				message = "해당 파일 업로드에 실패했습니다 : " + file.getOriginalFilename() + "!";
				return new ResponseEntity<String>(message, HttpStatus.EXPECTATION_FAILED);
			}
		}
		message = "하나의 엑셀 파일만 업로드 해주세요";
		return new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = "/excel/excelView")
	public String excelView() {
		return "excel/excelView"; 
	}
}
