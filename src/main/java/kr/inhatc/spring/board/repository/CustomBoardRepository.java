package kr.inhatc.spring.board.repository;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.BoardFileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBoardRepository {

  /**
   * 게시판 페이징 목록
   * @param searchVal
   * @param pageable
   * @return
   */
  Page<BoardDto> selectBoardList(String searchVal, Pageable pageable);

  /**
   * 게시판 상세 첨부파일 조회
   * @param boardId
   * @return
   */
  List<BoardFileDto> selectBoardFileDetail(Long boardId);
}
