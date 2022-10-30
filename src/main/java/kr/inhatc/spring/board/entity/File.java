package kr.inhatc.spring.board.entity;

import kr.inhatc.spring.utils.audit.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public class File extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;                    //id

    @Column(nullable = false)
    private String originFileName;      //원본 파일명

    @Column(nullable = false)
    private String savedFileName;       //저장된 파일명

    private String uploadDir;           //경로명

    private String extension;           //확장자

    private Long size;                  //파일 사이즈

    private String contentType;         //ContentType

    @OneToOne(mappedBy = "file", fetch = FetchType.LAZY)
    private BoardFile boardFile;

    @Builder
    public File(Long id, String originFileName, String savedFileName
            , String uploadDir, String extension, Long size, String contentType){
        this.id = id;
        this.originFileName = originFileName;
        this.savedFileName = savedFileName;
        this.uploadDir = uploadDir;
        this.extension = extension;
        this.size = size;
        this.contentType = contentType;
    }
}
