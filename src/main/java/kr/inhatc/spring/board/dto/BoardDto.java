package kr.inhatc.spring.board.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.querydsl.core.annotations.QueryProjection;

import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.board.service.BoardService;
import kr.inhatc.spring.member.entity.Member;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public class BoardDto {
  
  private Long id;                    //번호

  @NotEmpty(message = "제목은 필수입니다.")
  private String title;               //제목
  
  private String content;             //내용 - 에디터 변경 처리 
    
  private Long viewCount;             //조회수

  private String username;            // 사용자 이름
  
  private LocalDateTime regTime;      //등록 날짜

  private LocalDateTime updateTime;   //수정 날짜

  public BoardDto() {}
  
  public BoardDto(String title, String content) {
    this.title = title;
    this.content = content;
  }

  @QueryProjection
  public BoardDto(Long id, String title, String content, Long viewCount, String username, LocalDateTime regTime , LocalDateTime updateTime){
      this.id = id;
      this.title = title;
      this.content = content;
      this.viewCount = viewCount;
      this.username = username;
      this.regTime = regTime;
      this.updateTime = updateTime;
  }
  
  public Board toEntity(Member member) { 
    
    log.info("======================> toEntity 수행" + member);
    log.info("======================> " + title + ", " + content);
    
    return new Board(this, member);
  }
}
