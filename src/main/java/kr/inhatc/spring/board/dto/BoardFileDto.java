package kr.inhatc.spring.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import kr.inhatc.spring.board.entity.BoardFile;
import kr.inhatc.spring.board.entity.File;
import lombok.Builder;
import lombok.Data;

@Data
public class BoardFileDto {

    private Long boardFileId;

    private Long id;

    private Long fileId;

    private Long boardId;

    private String originFileName;

    private Long size;

    private String extension;

    public BoardFileDto(){

    }

    @Builder
    public BoardFileDto(Long boardId){
        this.boardId = boardId;
    }


    
    /**
     * 파일 정보 받아서 처리
     * @param file
     * @return
     */
    public BoardFile toEntity(File file){
        return BoardFile.builder()
                .boardId(boardId)
                .file(file)
                .build();
    }

    @QueryProjection
    public BoardFileDto(Long boardFileId, Long fileId, String originFileName, Long size, String extension){
        this.boardFileId = boardFileId;
        this.fileId = fileId;
        this.originFileName = originFileName;
        this.size = size;
        this.extension = extension;
    }
}
