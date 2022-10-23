package kr.inhatc.spring.files.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.inhatc.spring.files.exception.StorageFileNotFoundException;
import kr.inhatc.spring.files.service.StorageService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FileUploadController {

  private final StorageService storageService;
  
  @GetMapping("/files/list")
  public String listUploadedFiles(Model model) throws IOException {
    
    List<String> files = storageService.loadAll().map(path -> MvcUriComponentsBuilder.fromMethodName(
        FileUploadController.class, "saveFile",
        path.getFileName().toString()).build().toUri().toString()).collect(Collectors.toList());
    
    model.addAttribute("files", files);
    
    return "files/uploadForm";
  }
  
  @GetMapping("/files/{filename:.+}")
  @ResponseBody
  public ResponseEntity<Resource> saveFile(@PathVariable String fileName){
      Resource file = storageService.loadAsResource(fileName);
      return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, 
              "attatchment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  
  @PostMapping("/files/list")
  public String handleFileUpload(@RequestParam("file") MultipartFile file,
          RedirectAttributes redirectAttributes) {

      storageService.store(file);
      redirectAttributes.addFlashAttribute("message",
              "업로드에 성공했습니다. " + file.getOriginalFilename() + "!");

      return "redirect:/";
  }
  
  @ExceptionHandler(StorageFileNotFoundException.class)
  public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException ex) {
      return ResponseEntity.notFound().build();
  }
}
