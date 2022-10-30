package kr.inhatc.spring.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.board.dto.BoardFileDto;
import kr.inhatc.spring.board.dto.QBoardDto;
import kr.inhatc.spring.board.dto.QBoardFileDto;
import lombok.extern.log4j.Log4j2;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.inhatc.spring.board.entity.QBoard.board;
import static kr.inhatc.spring.board.entity.QBoardFile.boardFile;
import static kr.inhatc.spring.member.entity.QMember.member;

@Repository
@Log4j2
public class BoardRepositoryImpl implements CustomBoardRepository{
  
  private final JPAQueryFactory jpaQueryFactory;

  public BoardRepositoryImpl(JPAQueryFactory jpaQueryFactory) {      
      this.jpaQueryFactory = jpaQueryFactory;
  }
  
  /**
   * 게시판 리스트 가져오기 
   */
  @Override
  public Page<BoardDto> selectBoardList(String searchVal, Pageable pageable) {
    List<BoardDto> content = getBoardMemberDtos(searchVal, pageable);
    Long count = getCount(searchVal);
    return new PageImpl<>(content, pageable, count);
  }

  @Override
  public List<BoardFileDto> selectBoardFileDetail(Long boardId) {
    
    log.info(">>>>>>>>>>>>>>> BoardRepositoryImpl : " + boardId);
    
    List<BoardFileDto> content = jpaQueryFactory
            .select(new QBoardFileDto(
                    boardFile.id
                    ,boardFile.file.id
                    ,boardFile.file.originFileName
                    ,boardFile.file.size
                    ,boardFile.file.extension
            ))
            .from(boardFile)
            .leftJoin(boardFile.file)
            .where(boardFile.boardId.eq(boardId))
            .where(boardFile.delYn.eq("N"))
            .fetch();

    return content;
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
            member.name,            
            board.regTime,
            board.updateTime
         ))
        .from(board)
        .leftJoin(board.member, member)
        .where(containsSearch(searchVal))
        .where(board.delYn.eq("N"))
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
        .where(containsSearch(searchVal))
        //.leftJoin(board.member, member)   //검색조건 최적화
        .fetchOne();
    return count;
  }

  /**
   * %키워드% 조회
   * @param searchVal
   * @return
   */
  private BooleanExpression containsSearch(String searchVal) {    
    return searchVal != null ? board.title.contains(searchVal) : null;
  }
  
}
