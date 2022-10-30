package kr.inhatc.spring.board.service;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.board.repository.BoardRepository;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;

  private final BoardFileService fileService;
  
  /**
   * 폼에서 전달된 게시판 정보 저장하기 
   * @param boardDto
   */
  public Long saveBoard(BoardDto boardDto) {
    log.info(">>>>> 게시판 저장 : " + boardDto);
    
    List<Member> memberList = memberRepository.findAll();    
    Member member = memberList.get(0);    
    Board board = null;
    
    // insert
    if(boardDto.getId() == null) {      
      board = boardDto.toEntity(member);      
      boardRepository.save(board);      
    }    
    
    // update
    else {
      log.info(">>>>> 게시판 수정 : " + boardDto);
      board = boardRepository.findById(boardDto.getId()).get();
      board.update(boardDto.getTitle(), boardDto.getContent());
    }
    
    log.info(">>>>> 파일 저장 전 : " + boardDto);
    
    // 파일 저장 
    try {
      log.info(">>>> 파일 저장");
      fileService.saveFile(boardDto, board.getId());
    } catch (Exception e) {
      log.info("saveBoard() 에서 예외 발생");
      throw new RuntimeException(e);
    }

    return board.getId();
  }

  /**
   * 게시판에서 번호에 해당 글 삭제하기  
   * @param id
   */
  public Board deleteBoard(Long id) {
    Board board = boardRepository.findById(id).get();
    
    //플래그값이 Y이면 논리삭제
    board.delete("Y");
    return board;    
  }

  public Board selectBoardDetail(Long id) {
    Board board = boardRepository.findById(id).get();
    board.updateViewCount(board.getViewCount());
    return board;    
  }
}
