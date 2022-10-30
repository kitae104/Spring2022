package kr.inhatc.spring.board.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.inhatc.spring.board.dto.BoardDto;
import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(exclude = "member")
public class Board extends BaseEntity { 

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "board_id")
  private Long id; // 번호

  private String title; // 제목
  private String content; // 내용 - 에디터 변경 처리

  private Long viewCount; // 조회수
  private String delYn; // 삭제여부

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  /**
   * 수정하기
   * 
   * @param title
   * @param content
   * @return
   */
  public Board update(String title, String content) {
    this.title = title;
    this.content = content;
    return this;
  }

  /**
   * 삭제하기
   * 
   * @param delYn
   * @return
   */
  public Board delete(String delYn) {
    this.delYn = delYn;
    return this;
  }

//  /**
//   * 게시글 생성하기
//   * 
//   * @param boardDto
//   * @param member
//   */
//  @Builder
//  public Board(BoardDto boardDto, Member member) {
//    this.title = boardDto.getTitle();
//    this.content = boardDto.getContent();
//    this.viewCount = 0L;
//    this.delYn = "N";
//    this.member = member;
//  }

  @Builder
  public Board(String title, String content, Member member) {
    this.title = title;
    this.content = content;
    this.viewCount = 0L;
    this.delYn = "N";
    this.member = member;
  }

  /**
   * 게시판 조회수 증가
   * 
   * @param viewCount
   * @return
   */
  public Board updateViewCount(Long viewCount) {
    this.viewCount = viewCount + 1;
    return this;
  }
}
