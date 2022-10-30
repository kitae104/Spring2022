package kr.inhatc.spring.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.inhatc.spring.board.entity.Board;
import kr.inhatc.spring.member.entity.Member;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<MultipartFile> multipartFile;

    
    public BoardDto() {
    }
    
//    @Builder
//    public BoardDto(Long id, String title, String content) {
//        this.id = id;
//        this.title = title;
//        this.content = content;        
//    }
//
    @Builder
    public BoardDto(Long id, String title, String content, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
    }

    @QueryProjection
    public BoardDto(Long id, String title, String content, Long viewCount, String username, LocalDateTime regTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.username = username;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

    public Board toEntity(Member member) {
        log.info(">>>> Board 엔티티 생성");
        return Board.builder()
              .member(member)
              .title(title)
              .content(content)
              .build();
    }

    /**
     * 파일 정보를 리스트로 가져오기
     * @return
     */
    public List<MultipartFile> getMultipartFile() {
        return multipartFile;
    }
}
