package kr.inhatc.spring.board.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.inhatc.spring.member.entity.Member;
import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;                    //번호

    private String title;               //제목
    private String content;             //내용 - 에디터 변경 처리 
    
    private LocalDateTime regDate;      //등록 날짜

    private LocalDateTime uptDate;      //수정 날짜

    private Long viewCount;             //조회수
    private String delYn;               //삭제여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Board update(String title, String content){
        this.title = title;
        this.content = content;
        return this;
    }

    public Board delete(String delYn){
        this.delYn = delYn;
        return this;
    }

    @Builder
    public Board(String title, String content, Member member){
        this.title = title;
        this.content = content;
        this.viewCount = 0L;
        this.delYn = "N";
        this.member = member;
    }
}

