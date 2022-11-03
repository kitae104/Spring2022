package kr.inhatc.spring.excel.controller;

import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.excel.entity.ExcelData;
import kr.inhatc.spring.excel.entity.ExcelTutorial;
import kr.inhatc.spring.excel.service.ExcelDataService;
import kr.inhatc.spring.excel.service.ExcelTutorialService;
import kr.inhatc.spring.excel.utils.ExcelHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelTutorialController
{

	private final ExcelTutorialService fileService;
	
	private final ExcelDataService excelDataService;

	@GetMapping(value = "/excelUpload")
	public String excelUpload()
	{
		return "excel/excelUpload";
	}

	@PostMapping("/upload")
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

	@PostMapping("/upload2")
    public @ResponseBody ResponseEntity uploadFile2(@RequestParam("file") MultipartFile file)
    {
        log.info(">> 엑셀 업로드 처리");

        String message = "";
        if (ExcelHelper.hasExcelFormat(file))
        {
            try
            {
                excelDataService.save(file);

                List<ExcelData> excelList = excelDataService.getAllExcelDatas();

                if (excelList.isEmpty())
                {
                    return new ResponseEntity<String>(message, HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<List<ExcelData>>(excelList, HttpStatus.OK);
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
	
	@GetMapping(value = "/excelView")
	public ResponseEntity<List<ExcelTutorial>> excelView()
	{
		try
		{
			List<ExcelTutorial> tutorials = fileService.getAllExcelTutorials();

			if (tutorials.isEmpty())
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			log.info(">> 추가된 엑셀 파일 : " + tutorials.size());
			
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} 
		catch (Exception e)
		{
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/download")
	public ResponseEntity<Resource> getFile(){
	  log.info(">> 엑셀 다운로드 수행");
	  
	  String filename = "test.xlsx";
	  InputStreamResource file = new InputStreamResource(fileService.load());
	  	  
	  return ResponseEntity.ok()
	      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	}
	
	@GetMapping(value = "/excelView2")
    public String excelView2() {
        return "excel/excelView"; 
    }
}
