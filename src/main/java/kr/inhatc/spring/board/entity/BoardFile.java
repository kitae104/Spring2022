package kr.inhatc.spring.board.entity;

import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BoardFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_file_id")
    private Long id;            //번호

    private Long boardId;
    private String delYn;

    @OneToOne  // file과 1:1 관계 설정
    @JoinColumn(name = "file_id")
    private File file;

    @Builder
    public BoardFile(Long boardId, Long fileId, String delYn, File file){
        this.boardId = boardId;
        this.delYn = "N";
        this.file = file;
    }

    public BoardFile delete(String delYn){
        this.delYn = delYn;
        return this;
    }
}
