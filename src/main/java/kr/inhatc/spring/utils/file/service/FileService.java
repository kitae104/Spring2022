package kr.inhatc.spring.utils.file.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {

    /**
     * 파일 업로드 처리 메소드<br>
     * 저장된 파일 이름 반환
     * @param uploadPath 업로드 파일 경로
     * @param originalFileName 파일명
     * @param fileData 바이트 형태 데이터
     * @return 저장된 파일 이름
     * @throws Exception
     */
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {

        UUID uuid = UUID.randomUUID();      // 다른 개체들을 구별하기 위해 사용 - 파일명 중복을 피하기 위해 사용
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // 새로운 이름으로 저장할 파일명
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //바이트 단위의 출력 - 파일 저장
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    /**
     * 파일을 삭제하는 메소드
     * @param filePath 삭제할 파일 경로
     * @throws Exception
     */
    public void deleteFile(String filePath) throws Exception {

        File deleteFile = new File(filePath);   // 경로를 이용해서 파일 객체 생성

        if(deleteFile.exists()){                // 파일 존재 확인 후
            deleteFile.delete();                // 파일 삭제
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
