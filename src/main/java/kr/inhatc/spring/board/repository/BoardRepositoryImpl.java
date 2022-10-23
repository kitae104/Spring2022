package kr.inhatc.spring.board.repository;

import static kr.inhatc.spring.board.entity.QBoard.board;
import static kr.inhatc.spring.member.entity.QMember.member;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.QBoardDto;

@Repository
public class BoardRepositoryImpl implements CustomBoardRepository{
  
  private final JPAQueryFactory jpaQueryFactory;

  public BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {      
      this.jpaQueryFactory = jpaQueryFactory;
  }
  
  @Override
  public Page<BoardDto> selectBoardList(String searchVal, Pageable pageable) {
    List<BoardDto> content = getBoardMemberDtos(searchVal, pageable);
    Long count = getCount(searchVal);
    return new PageImpl<>(content, pageable, count);
  }

  /**
   * 검색어에 대한 게시판 리스트 가져오기 
   * @param searchVal
   * @param pageable
   * @return
   */
  private List<BoardDto> getBoardMemberDtos(String searchVal, Pageable pageable) {
    
    List<BoardDto> content = jpaQueryFactory
        .select(new QBoardDto(
            board.id,
            board.title,
            board.content,
            board.viewCount,
            member.name            
         ))
        .from(board)
        .leftJoin(board.member, member)
        .orderBy(board.id.desc())               // 최신 정보가 윗쪽으로
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    
    return content; 
  }


  /**
   * 검색어에 대한 항목 갯수 가져오기 
   * @param searchVal
   * @return
   */
  private Long getCount(String searchVal) {
    Long count = jpaQueryFactory
        .select(board.count())
        .from(board)
        //.leftJoin(board.member, member)   //검색조건 최적화
        .fetchOne();
    return count;
  }

}
