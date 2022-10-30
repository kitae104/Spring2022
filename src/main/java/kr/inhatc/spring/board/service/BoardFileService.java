package kr.inhatc.spring.board.service;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.BoardFileDto;
import kr.inhatc.spring.board.dto.FileDto;
import kr.inhatc.spring.board.entity.BoardFile;
import kr.inhatc.spring.board.repository.BoardFileRepository;
import kr.inhatc.spring.board.repository.FileRepository;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardFileService
{
	@Value("${upload.board.path}")
    private String uploadDir;
	
	private final FileRepository fileRepository;

	private final BoardFileRepository boardFileRepository;

	private final MemberRepository memberRepository;

	@Transactional
	public Map<String, Object> saveFile(BoardDto boardDto, Long boardId) throws Exception {
		
		List<MultipartFile> multipartFile = boardDto.getMultipartFile();
		log.info(">>>>>>>>>>>>>>> 전달된 파일 갯수 : " + multipartFile.size());
		//결과 Map
		Map<String, Object> result = new HashMap<String, Object>();

		//파일 시퀀스 리스트
		List<Long> fileIds = new ArrayList<Long>();

		try {
			if (multipartFile != null) {
				if (multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {

					// 파일 정보를 읽어와서 처리하기
					for (MultipartFile file1 : multipartFile) {
						String originalFileName = file1.getOriginalFilename();    //오리지날 파일명
						String extension = originalFileName.substring(originalFileName.lastIndexOf("."));    //파일 확장자
						String savedFileName = UUID.randomUUID() + extension;    //저장될 파일 명
						log.info(">>>> 파일 경로 : " + uploadDir + savedFileName);  
						File targetFile = new File(uploadDir + savedFileName);

						//초기값으로 fail 설정
						result.put("result", "FAIL"); 

						FileDto fileDto = FileDto.builder()
								.originFileName(originalFileName)
								.savedFileName(savedFileName)
								.uploadDir(uploadDir)
								.extension(extension)
								.size(file1.getSize())
								.contentType(file1.getContentType())
								.build();

						//파일 insert
						kr.inhatc.spring.board.entity.File file = fileDto.toEntity();
						Long fileId = insertFile(file);
						log.info("fileId={}", fileId);

						try {
							InputStream fileStream = file1.getInputStream();
							FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
							//배열에 담기
							fileIds.add(fileId);
							result.put("fileIdxs", fileIds.toString());
							result.put("result", "OK");
						} catch (Exception e) {
							//파일삭제
							FileUtils.deleteQuietly(targetFile);    //저장된 현재 파일 삭제
							e.printStackTrace();
							result.put("result", "FAIL");
							break;
						}

						BoardFileDto boardFileDto = BoardFileDto.builder()
								.boardId(boardId)
								.build();

						BoardFile boardFile = boardFileDto.toEntity(file);
						insertBoardFile(boardFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 파일 저장
	 * @param file
	 * @return
	 */
	@Transactional
	public Long insertFile(kr.inhatc.spring.board.entity.File file) {

		return fileRepository.save(file).getId();
	}

	@Transactional
	public Long insertBoardFile(BoardFile boardFile) {

		return boardFileRepository.save(boardFile).getId();

	}

	@Transactional
	public BoardFile deleteBoardFile(Long boardFileId){
		BoardFile boardFile = boardFileRepository.findById(boardFileId).get();
		
		log.info(">>>>> deleteBoardFile : " + boardFile);
		
		//삭제
		boardFile.delete("Y");
		return boardFile;
	}
}
