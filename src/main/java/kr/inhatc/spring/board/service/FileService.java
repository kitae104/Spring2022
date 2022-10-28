package kr.inhatc.spring.board.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileService
{
	@Value("${upload.path}")
    private String uploadDir;
	
	private final FileRepository fileRepository;
	
	@Transactional
	public Map<String, Object> saveFile(BoardDto boardDto) throws Exception {
		
		List<MultipartFile> multipartFile = boardDto.getMultipartFile();
	}
	 
}
