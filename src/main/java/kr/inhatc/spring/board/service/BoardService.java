package kr.inhatc.spring.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.inhatc.spring.board.controller.BoardController;
import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.board.repository.BoardRepository;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;
  
  /**
   * 폼에서 전달된 게시판 정보 저장하기 
   * @param boardDto
   */
  public Long saveBoard(BoardDto boardDto) {
    
    
    List<Member> memberList = memberRepository.findAll();    
    Member member = memberList.get(0);    
    Board board = null;
    
    // insert
    if(boardDto.getId() == null) {
      board = boardDto.toEntity(member);
      log.info("======================> " + board);
      boardRepository.save(board);
    }    
    
    // update
    else {
      board = boardRepository.findById(boardDto.getId()).get();
      board.update(boardDto.getTitle(), boardDto.getContent());
    }
    
    // 파일 저장 
    fileService.saveFile(boardDto);
    
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
