package kr.inhatc.spring.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.inhatc.spring.board.dto.BoardDto;

public interface CustomBoardRepository {
  
  Page<BoardDto> selectBoardList(String searchVal, Pageable pageable);
}
