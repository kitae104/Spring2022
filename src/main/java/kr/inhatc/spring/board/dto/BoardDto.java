package kr.inhatc.spring.board.dto;

import javax.validation.constraints.NotEmpty;

import com.querydsl.core.annotations.QueryProjection;

import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.member.entity.Member;
import lombok.Data;

@Data
public class BoardDto {
  
  private Long id;                    //번호

  @NotEmpty(message = "제목은 필수입니다.")
  private String title;               //제목
  
  private String content;             //내용 - 에디터 변경 처리 
    
  private Long viewCount;             //조회수

  private String username;            // 사용자 이름

  public BoardDto() {}
  
  public BoardDto(String title, String content) {
    this.title = title;
    this.content = content;
  }

  @QueryProjection
  public BoardDto(Long id, String title, String content, Long viewCount, String username){
      this.id = id;
      this.title = title;
      this.content = content;
      this.viewCount = viewCount;
      this.username = username;
  }
  
  public Board toEntity(Member member) { 
    return Board.builder()
            .member(member)
            .title(title)
            .content(content)
            .build();
  }
}
