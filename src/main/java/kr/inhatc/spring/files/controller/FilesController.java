package kr.inhatc.spring.files.controller;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import kr.inhatc.spring.files.dto.FileInfoDto;
import kr.inhatc.spring.files.upload.message.ResponseMessage;
import kr.inhatc.spring.files.upload.service.FilesStorageService;

@Controller
//@CrossOrigin("http://localhost:8001")
public class FilesController {

  @Autowired
  private FilesStorageService storageService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";
    try {
      storageService.save(file);
      
      message = "파일 업로드 성공 : " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    } catch (Exception e) {
      message = "파일 업로드 실패 : " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }
  
  @GetMapping("/files")
  public ResponseEntity<List<FileInfoDto>> getListFiles(){
	  List<FileInfoDto> fileInfos = storageService.loadAll().map(path -> {
		 String fileName = path.getFileName().toString();
		 String url = MvcUriComponentsBuilder.fromMethodName(FilesController.class, "getFile", path.getFileName().toString())
				 .build().toString();
		 return new FileInfoDto(fileName, url);
	  }).collect(Collectors.toList());
	  return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }
  
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable String fileName){
	  Resource file = storageService.load(fileName);
	  return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, 
			  "attatchment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  
  
}
